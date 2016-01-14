package org.snapscript.web.binary.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.snapscript.web.binary.event.ProcessEvent;
import org.snapscript.web.binary.event.ProcessEventChannel;
import org.snapscript.web.binary.event.ProcessEventConnection;
import org.snapscript.web.binary.event.ProcessEventConsumer;
import org.snapscript.web.binary.event.ProcessEventListener;
import org.snapscript.web.binary.event.ProcessEventProducer;
import org.snapscript.web.binary.event.ExecuteEvent;
import org.snapscript.web.binary.event.ExitEvent;
import org.snapscript.web.binary.event.PingEvent;
import org.snapscript.web.binary.event.PongEvent;
import org.snapscript.web.binary.event.RegisterEvent;
import org.snapscript.web.binary.event.SyntaxErrorEvent;
import org.snapscript.web.binary.event.WriteErrorEvent;
import org.snapscript.web.binary.event.WriteOutputEvent;

public class SocketEventServer implements ProcessEventChannel {

   private final Map<String, ProcessEventChannel> receivers;
   private final SocketAcceptor acceptor;
   private final ProcessEventListener listener;
   
   public SocketEventServer(ProcessEventListener listener, int port) throws IOException {
      this.receivers = new ConcurrentHashMap<String, ProcessEventChannel>();
      this.acceptor = new SocketAcceptor(port);
      this.listener = listener;
   }
   
   @Override
   public void send(ProcessEvent event) throws Exception {
      String process = event.getProcess();
      ProcessEventChannel channel = receivers.get(process);
      
      if(channel == null) {
         throw new IllegalArgumentException("No channel for " + process);
      }
      channel.send(event);
   }
   
   public void start() throws Exception {
      acceptor.start();
   }
   
   @Override
   public void close() throws Exception {
      acceptor.close();
   }
   
   private class SocketAcceptor implements Runnable {
      
      private final ServerSocket server;
      private final Thread thread;
      
      public SocketAcceptor(int port) throws IOException {
         this.server = new ServerSocket(port);
         this.thread = new Thread(this);
      }
      
      @Override
      public void run() {
         try {
            while(true) {
               Socket socket = server.accept();
               InputStream input = socket.getInputStream();
               OutputStream output = socket.getOutputStream();
               
               try {
                  SocketConnection connection = new SocketConnection(socket, input, output);
               
                  socket.setSoTimeout(10000);
                  connection.start();
               } catch(Exception e) {
                  socket.close();
               }
            }
         }catch(Exception e) {
            e.printStackTrace();
         }
      }
      
      public void start() {
         try {
            thread.start();
         }catch(Exception e){
            e.printStackTrace();
         }
      }
      
      public void close() {
         try {
            server.close();
         }catch(Exception e){
            e.printStackTrace();
         }
      }
   }
   
   private class SocketConnection extends Thread implements ProcessEventChannel {
      
      private final ProcessEventConnection connection;
      private final Socket socket;
      
      public SocketConnection(Socket socket, InputStream input, OutputStream output) throws IOException {
         this.connection = new ProcessEventConnection(input, output);
         this.socket = socket;
      }
      
      @Override
      public void send(ProcessEvent event) throws Exception {
         String process = event.getProcess();
         ProcessEventProducer producer = connection.getProducer();
         
         try {
            producer.produce(event);
         } catch(Exception e) {
            e.printStackTrace();
            receivers.remove(process);
            close();
         }
      }
      
      @Override
      public void run() {
         try {
            ProcessEventConsumer consumer = connection.getConsumer();
            
            while(true) {
               ProcessEvent value = consumer.consume();
               String process = value.getProcess();
               
               receivers.put(process, this);
               
               if(value instanceof ExitEvent) {
                  listener.onExit(this, (ExitEvent)value);
               } else if(value instanceof ExecuteEvent) {
                  listener.onExecute(this, (ExecuteEvent)value);                  
               } else if(value instanceof RegisterEvent) {
                  listener.onRegister(this, (RegisterEvent)value);
               } else if(value instanceof SyntaxErrorEvent) {
                  listener.onSyntaxError(this, (SyntaxErrorEvent)value);
               } else if(value instanceof WriteErrorEvent) {
                  listener.onWriteError(this, (WriteErrorEvent)value);
               } else if(value instanceof WriteOutputEvent) {
                  listener.onWriteOutput(this, (WriteOutputEvent)value);
               } else if(value instanceof PingEvent) {
                  listener.onPing(this, (PingEvent)value);
               } else if(value instanceof PongEvent) {
                  listener.onPong(this, (PongEvent)value);
               }
            }
         }catch(Exception e) {
            e.printStackTrace();
            close();
         }
      }
      
      public void close() {
         try {
            listener.onClose(this);
            socket.close();
         } catch(Exception e) {
            e.printStackTrace();
         }
      }
   }
}
