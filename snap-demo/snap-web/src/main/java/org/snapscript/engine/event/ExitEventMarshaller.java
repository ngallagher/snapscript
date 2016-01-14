package org.snapscript.engine.event;

import static org.snapscript.engine.event.ProcessEventType.EXIT;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.snapscript.engine.message.BinaryMessage;

public class ExitEventMarshaller implements ProcessEventMarshaller<ExitEvent> {

   @Override
   public ExitEvent fromMessage(BinaryMessage message) throws IOException {
      byte[] array = message.getData();
      int length = message.getLength();
      int offset = message.getOffset();
      ByteArrayInputStream buffer = new ByteArrayInputStream(array, offset, length);
      DataInputStream input = new DataInputStream(buffer);
      String process = input.readUTF();
      
      return new ExitEvent(process);
      
   }

   @Override
   public BinaryMessage toMessage(ExitEvent value) throws IOException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      DataOutputStream output = new DataOutputStream(buffer);
      String process = value.getProcess();
      
      output.writeUTF(process);
      output.flush();
      
      byte[] array = buffer.toByteArray();
      return new BinaryMessage(process, EXIT.code, array, 0, array.length);
   }
}
