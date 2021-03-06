package org.snapscript.agent.event;

import static org.snapscript.agent.event.ProcessEventType.REGISTER;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegisterEventMarshaller implements ProcessEventMarshaller<RegisterEvent> {

   @Override
   public RegisterEvent fromMessage(MessageEnvelope message) throws IOException {
      byte[] array = message.getData();
      int length = message.getLength();
      int offset = message.getOffset();
      ByteArrayInputStream buffer = new ByteArrayInputStream(array, offset, length);
      DataInputStream input = new DataInputStream(buffer);
      String process = input.readUTF();
      String system = input.readUTF();
      
      return new RegisterEvent(process, system);
   }

   @Override
   public MessageEnvelope toMessage(RegisterEvent event) throws IOException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      DataOutputStream output = new DataOutputStream(buffer);
      String process = event.getProcess();
      String system = event.getSystem();
      
      output.writeUTF(process);
      output.writeUTF(system);
      output.flush();
      
      byte[] array = buffer.toByteArray();
      return new MessageEnvelope(process, REGISTER.code, array, 0, array.length);
   }
}
