package org.snapscript.web.message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicReference;

public class MessagePublisher {
   
   private final AtomicReference<String> process;
   private final DataOutputStream stream;
   
   public MessagePublisher(AtomicReference<String> process, OutputStream stream) {
      this.stream = new DataOutputStream(stream);
      this.process = process;
   }

   public synchronized void publish(MessageType type, byte[] octets, int offset, int length) throws IOException {
      String processId = process.get();
      Message message = new Message(type, processId, octets, offset, length);
      Message.writeMessage(message, stream);
   }
   
   public synchronized void publish(MessageType type, Object value) throws IOException {
      String text = String.valueOf(value);
      byte[] bytes = text.getBytes("UTF-8");
      String processId = process.get();
      Message message = new Message(type, processId, bytes);
      Message.writeMessage(message, stream);
   }

}
