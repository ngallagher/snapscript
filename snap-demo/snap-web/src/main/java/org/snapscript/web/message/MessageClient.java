package org.snapscript.web.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

public class MessageClient implements MessageListener {

   private final Set<MessageListener> listeners;
   private final AtomicReference<String> process;
   private final MessagePublisher publisher;
   private final MessageReceiver receiver;
   private final DataOutputStream output;
   private final DataInputStream input;
   private final Socket socket;
   
   public MessageClient(String process, Socket socket) throws IOException {
      this.output = new DataOutputStream(socket.getOutputStream());
      this.input = new DataInputStream(socket.getInputStream());
      this.process = new AtomicReference<String>(process);
      this.publisher = new MessagePublisher(this.process, output);
      this.receiver = new MessageReceiver(this, input, socket);
      this.listeners = new CopyOnWriteArraySet<MessageListener>();
      this.socket = socket;
   }
   
   public void update(String processId){
      process.set(processId);
   }
   
   public void start(){
      receiver.start();
   }
   
   public void close() throws IOException{
      socket.close();
   }
   
   public void register(MessageListener listener){
      listeners.add(listener);
   }
   
   public void setTimeout(int value) throws SocketException{
      socket.setSoTimeout(value);
   }
   
   public String getProcessId(){
      return process.get();
   }
   
   public MessagePublisher getPublisher(){
      return publisher;
   }

   @Override
   public void onMessage(Message message) {
      for(MessageListener listener : listeners){
         listener.onMessage(message);
      }
   }

   @Override
   public void onError(Exception cause) {
      for(MessageListener listener : listeners){
         listener.onError(cause);
      }
   }

   @Override
   public void onClose() {
      for(MessageListener listener : listeners){
         listener.onClose();
      }
   }
   
}
