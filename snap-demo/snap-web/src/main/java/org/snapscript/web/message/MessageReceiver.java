package org.snapscript.web.message;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageReceiver implements Runnable {

   private final MessageListener listener;
   private final AtomicBoolean active;
   private final Socket socket;
   
   public MessageReceiver(MessageListener listener, Socket socket) {
      this.active = new AtomicBoolean();
      this.listener = listener;
      this.socket = socket;
   }

   @Override
   public void run() {
      try { 
         socket.setSoTimeout(0); // wait forever
         InputStream stream = socket.getInputStream();
         DataInputStream input = new DataInputStream(stream);
         
         while(active.get()) {
            Message message = Message.readMessage(input);
            if(message != null) {
               listener.onMessage(message);
            }
         }
      } catch(Exception e) {
         listener.onError(e);
      } finally {
         listener.onClose();
         active.set(false);
      }
   }
   
   public void start() {
      if(active.compareAndSet(false, true)) {
         Thread thread = new Thread(this);
         thread.start();
      }
   }
}
