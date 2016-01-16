package org.snapscript.engine.event;

import static org.snapscript.engine.event.ProcessEventType.RESUME;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ResumeEventMarshaller implements ProcessEventMarshaller<ResumeEvent> {

   @Override
   public ResumeEvent fromMessage(MessageEnvelope message) throws IOException {
      byte[] array = message.getData();
      int length = message.getLength();
      int offset = message.getOffset();
      ByteArrayInputStream buffer = new ByteArrayInputStream(array, offset, length);
      DataInputStream input = new DataInputStream(buffer);
      String process = input.readUTF();
      String thread = input.readUTF();
      
      return new ResumeEvent(process, thread);
   }

   @Override
   public MessageEnvelope toMessage(ResumeEvent event) throws IOException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      DataOutputStream output = new DataOutputStream(buffer);
      String process = event.getProcess();
      String thread = event.getThread();
      
      output.writeUTF(process);
      output.writeUTF(thread);
      output.flush();
      
      byte[] array = buffer.toByteArray();
      return new MessageEnvelope(process, RESUME.code, array, 0, array.length);
   }
}
