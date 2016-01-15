package org.snapscript.engine.event;

import static org.snapscript.engine.event.ProcessEventType.WRITE_OUTPUT;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WriteOutputEventMarshaller implements ProcessEventMarshaller<WriteOutputEvent> {

   @Override
   public WriteOutputEvent fromMessage(MessageEnvelope message) throws IOException {
      byte[] array = message.getData();
      int length = message.getLength();
      int offset = message.getOffset();
      ByteArrayInputStream buffer = new ByteArrayInputStream(array, offset, length);
      DataInputStream input = new DataInputStream(buffer);
      String process = input.readUTF();
      int size = input.readInt();
      byte[] chunk = new byte[size];
      
      input.readFully(chunk, 0, size);
      
      return new WriteOutputEvent(process, chunk, 0, size);
   }

   @Override
   public MessageEnvelope toMessage(WriteOutputEvent value) throws IOException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      DataOutputStream output = new DataOutputStream(buffer);
      String process = value.getProcess();
      byte[] chunk = value.getData();
      int length = value.getLength();
      int offset = value.getOffset();
      
      output.writeUTF(process);
      output.writeInt(length);
      output.write(chunk, offset, length);
      output.flush();
      
      byte[] array = buffer.toByteArray();
      return new MessageEnvelope(process, WRITE_OUTPUT.code, array, 0, array.length);
   }
}