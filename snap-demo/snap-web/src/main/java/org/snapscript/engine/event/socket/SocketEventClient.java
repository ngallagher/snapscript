package org.snapscript.engine.event.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
import org.snapscript.engine.event.SyntaxErrorEvent;
import org.snapscript.engine.event.WriteErrorEvent;
import org.snapscript.engine.event.WriteOutputEvent;

public class SocketEventClient {
   
   private final ProcessEventListener listener;
   
   public SocketEventClient(ProcessEventListener listener) throws IOException {
      this.listener = listener;
   }
   
   public ProcessEventChannel connect(int port) throws Exception {
      Socket socket = new Socket("localhost", port);
      InputStream input = socket.getInputStream();
      OutputStream output = socket.getOutputStream();
      SocketConnection connection = new SocketConnection(socket, input, output);
   
      socket.setSoTimeout(10000);
      connection.start();
      return connection;
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
      
      public void send(ProcessEvent event) throws Exception {
         ProcessEventProducer producer = connection.getProducer();
         
         try {
            producer.produce(event);
         } catch(Exception e) {
            e.printStackTrace();
            close();
         }
      }
      
      @Override
      public void run() {
         try {
            ProcessEventConsumer consumer = connection.getConsumer();
            
            while(true) {
               ProcessEvent value = consumer.consume();
               
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
         } finally {
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
