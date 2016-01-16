package org.snapscript.engine.event.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.engine.event.ExecuteEvent;
import org.snapscript.engine.event.ExitEvent;
import org.snapscript.engine.event.PingEvent;
import org.snapscript.engine.event.PongEvent;
import org.snapscript.engine.event.ProcessEvent;
import org.snapscript.engine.event.ProcessEventChannel;
import org.snapscript.engine.event.ProcessEventConnection;
import org.snapscript.engine.event.ProcessEventConsumer;
import org.snapscript.engine.event.ProcessEventListener;
import org.snapscript.engine.event.ProcessEventProducer;
import org.snapscript.engine.event.RegisterEvent;
import org.snapscript.engine.event.ResumeEvent;
import org.snapscript.engine.event.ScopeEvent;
import org.snapscript.engine.event.SyntaxErrorEvent;
import org.snapscript.engine.event.WriteErrorEvent;
import org.snapscript.engine.event.WriteOutputEvent;

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
   public boolean send(ProcessEvent event) throws Exception {
      String process = event.getProcess();
      ProcessEventChannel channel = receivers.get(process);
      
      if(channel == null) {
         throw new IllegalArgumentException("No channel for " + process);
      }
      return channel.send(event);
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
      private final AtomicBoolean open;
      private final Socket socket;
      
      public SocketConnection(Socket socket, InputStream input, OutputStream output) throws IOException {
         this.connection = new ProcessEventConnection(input, output);
         this.open = new AtomicBoolean(true);
         this.socket = socket;
      }
      
      @Override
      public boolean send(ProcessEvent event) throws Exception {
         String process = event.getProcess();
         ProcessEventProducer producer = connection.getProducer();
         
         try {
            producer.produce(event);
            return true;
         } catch(Exception e) {
            e.printStackTrace();
            receivers.remove(process);
            close();
         }
         return false;
      }
      
      @Override
      public void run() {
         try {
            ProcessEventConsumer consumer = connection.getConsumer();
            
            while(true) {
               ProcessEvent event = consumer.consume();
               String process = event.getProcess();
               
               receivers.put(process, this);
               
               if(event instanceof ExitEvent) {
                  listener.onExit(this, (ExitEvent)event);
               } else if(event instanceof ExecuteEvent) {
                  listener.onExecute(this, (ExecuteEvent)event);                  
               } else if(event instanceof RegisterEvent) {
                  listener.onRegister(this, (RegisterEvent)event);
               } else if(event instanceof SyntaxErrorEvent) {
                  listener.onSyntaxError(this, (SyntaxErrorEvent)event);
               } else if(event instanceof WriteErrorEvent) {
                  listener.onWriteError(this, (WriteErrorEvent)event);
               } else if(event instanceof WriteOutputEvent) {
                  listener.onWriteOutput(this, (WriteOutputEvent)event);
               } else if(event instanceof PingEvent) {
                  listener.onPing(this, (PingEvent)event);
               } else if(event instanceof PongEvent) {
                  listener.onPong(this, (PongEvent)event);
               } else if(event instanceof ScopeEvent) {
                  listener.onScope(this, (ScopeEvent)event);
               } else if(event instanceof ResumeEvent) {
                  listener.onResume(this, (ResumeEvent)event);
               }
            }
         }catch(Exception e) {
            e.printStackTrace();
         }finally {
            close();
         }
      }
      
      public void close() {
         try {
            if(open.compareAndSet(true, false)) {
               listener.onClose(this);
            }
            socket.close();
         } catch(Exception e) {
            e.printStackTrace();
         }
      }
   }
}
