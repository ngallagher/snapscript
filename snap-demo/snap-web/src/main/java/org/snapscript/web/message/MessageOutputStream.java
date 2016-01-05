package org.snapscript.web.message;

import java.io.IOException;
import java.io.OutputStream;

public class MessageOutputStream extends OutputStream {

   private final MessageClient client;
   private final MessageType type;
   
   public MessageOutputStream(MessageType type, MessageClient client) {
      this.client = client;
      this.type = type;
   }
   
   public void write(int octet) throws IOException {
      write(new byte[]{(byte)octet});
   }
   
   public void write(byte[] octets) throws IOException {
      write(octets, 0, octets.length);
   }
   
   public void write(byte[] octets, int offset, int length) throws IOException {
      client.getPublisher().publish(type, octets, offset, length);
   }
   
}
