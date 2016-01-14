package org.snapscript.web.binary.event;

import static org.snapscript.web.binary.event.ProcessEventType.SYNTAX_ERROR;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.snapscript.web.binary.BinaryMessage;

public class SyntaxErrorEventMarshaller implements ProcessEventMarshaller<SyntaxErrorEvent> {

   @Override
   public SyntaxErrorEvent fromMessage(BinaryMessage message) throws IOException {
      byte[] array = message.getData();
      int length = message.getLength();
      int offset = message.getOffset();
      ByteArrayInputStream buffer = new ByteArrayInputStream(array, offset, length);
      DataInputStream input = new DataInputStream(buffer);
      String process = input.readUTF();
      String resource = input.readUTF();
      int line = input.readInt();
      
      return new SyntaxErrorEvent(process, resource, line);
   }

   @Override
   public BinaryMessage toMessage(SyntaxErrorEvent value) throws IOException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      DataOutputStream output = new DataOutputStream(buffer);
      String process = value.getProcess();
      String resource = value.getResource();
      int line = value.getLine();
      
      output.writeUTF(process);
      output.writeUTF(resource);
      output.writeInt(line);
      output.flush();
      
      byte[] array = buffer.toByteArray();
      return new BinaryMessage(process, SYNTAX_ERROR.code, array, 0, array.length);
   }
}

