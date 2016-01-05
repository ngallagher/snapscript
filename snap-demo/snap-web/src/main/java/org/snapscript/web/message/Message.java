package org.snapscript.web.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.simpleframework.common.encode.Base64Encoder;

public class Message {

   private final MessageType type;
   private final String process;
   private final byte[] octets;
   private final int offset;
   private final int length;
   
   public Message(MessageType type, String process) {
      this(type, process, new byte[]{});
   }
   
   public Message(MessageType type, String process, byte[] octets) {
      this(type, process, octets, 0, octets.length);
   }
   
   public Message(MessageType type, String process, byte[] octets, int offset, int length) {
      this.type = type;
      this.process = process;
      this.octets = octets;
      this.offset = offset;
      this.length = length;
   }
   
   public String getProcessId(){
      return process;
   }
   
   public MessageType getType(){
      return type;
   }
   
   public String getData(String charset) {
      try {
         return new String(octets, offset, length, charset);
      } catch(Exception e) {
         throw new IllegalStateException("Error encoding", e);
      }
   }
   
   public byte[] getData() {
      return octets;
   }
   
   public int getLength(){
      return length;
   }
   
   public int getOffset() {
      return offset;
   }
   
   public static void writeMessage(Message message, DataOutputStream output) throws IOException{
      String header = message.type.name();
      output.writeUTF(header);
      output.writeUTF(message.process);
      if(message.length == 0) {
         output.writeBoolean(false);
      } else {
         output.writeBoolean(true);
         char[] encoded = Base64Encoder.encode(message.octets, message.offset, message.length);
         String text = new String(encoded);
         output.writeUTF(text);
      }
      output.flush();
   }
   
   public static Message readMessage(DataInputStream input) throws IOException {
      String header = input.readUTF();
      String process = input.readUTF();
      MessageType type = Enum.valueOf(MessageType.class, header);
      if(input.readBoolean()) {
         String text = input.readUTF();
         char[] encoded = text.toCharArray();
         byte[] data = Base64Encoder.decode(encoded);
         
         return new Message(type, process, data);
      }
      return new Message(type, process);
   }
}
