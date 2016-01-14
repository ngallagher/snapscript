package org.snapscript.web.binary.event;

import static org.snapscript.web.binary.event.ProcessEventType.PONG;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.snapscript.web.binary.BinaryMessage;

public class PongEventMarshaller implements ProcessEventMarshaller<PongEvent> {

   @Override
   public PongEvent fromMessage(BinaryMessage message) throws IOException {
      byte[] array = message.getData();
      int length = message.getLength();
      int offset = message.getOffset();
      ByteArrayInputStream buffer = new ByteArrayInputStream(array, offset, length);
      DataInputStream input = new DataInputStream(buffer);
      String process = input.readUTF();
      
      return new PongEvent(process);
   }

   @Override
   public BinaryMessage toMessage(PongEvent value) throws IOException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      DataOutputStream output = new DataOutputStream(buffer);
      String process = value.getProcess();
      
      output.writeUTF(process);
      output.flush();
      
      byte[] array = buffer.toByteArray();
      return new BinaryMessage(process, PONG.code, array, 0, array.length);
   }
}

