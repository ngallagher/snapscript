package org.snapscript.engine.event;

import static org.snapscript.engine.event.ProcessEventType.PING;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PingEventMarshaller implements ProcessEventMarshaller<PingEvent> {

   @Override
   public PingEvent fromMessage(MessageEnvelope message) throws IOException {
      byte[] array = message.getData();
      int length = message.getLength();
      int offset = message.getOffset();
      ByteArrayInputStream buffer = new ByteArrayInputStream(array, offset, length);
      DataInputStream input = new DataInputStream(buffer);
      String process = input.readUTF();
      
      return new PingEvent(process);
   }

   @Override
   public MessageEnvelope toMessage(PingEvent event) throws IOException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      DataOutputStream output = new DataOutputStream(buffer);
      String process = event.getProcess();
      
      output.writeUTF(process);
      output.flush();
      
      byte[] array = buffer.toByteArray();
      return new MessageEnvelope(process, PING.code, array, 0, array.length);
   }
}
