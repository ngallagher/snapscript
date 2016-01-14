package org.snapscript.web.binary.event;

import static org.snapscript.web.binary.event.ProcessEventType.WRITE_OUTPUT;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.snapscript.web.binary.BinaryMessage;

public class WriteOutputEventMarshaller implements ProcessEventMarshaller<WriteOutputEvent> {

   @Override
   public WriteOutputEvent fromMessage(BinaryMessage message) throws IOException {
      byte[] array = message.getData();
      int length = message.getLength();
      int offset = message.getOffset();
      ByteArrayInputStream buffer = new ByteArrayInputStream(array, offset, length);
      DataInputStream input = new DataInputStream(buffer);
      String process = input.readUTF();
      int size = input.readInt();
      byte[] chunk = new byte[size];
      
      input.readFully(chunk, 0, size);
      
      return new WriteOutputEvent(process, array, 0, size);
   }

   @Override
   public BinaryMessage toMessage(WriteOutputEvent value) throws IOException {
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
      return new BinaryMessage(process, WRITE_OUTPUT.code, array, 0, array.length);
   }
}