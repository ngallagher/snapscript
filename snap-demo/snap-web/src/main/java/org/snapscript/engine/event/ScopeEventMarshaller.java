package org.snapscript.engine.event;

import static org.snapscript.engine.event.ProcessEventType.SCOPE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ScopeEventMarshaller implements ProcessEventMarshaller<ScopeEvent> {

   @Override
   public ScopeEvent fromMessage(MessageEnvelope message) throws IOException {
      byte[] array = message.getData();
      int length = message.getLength();
      int offset = message.getOffset();
      Map<String, String> variables = new HashMap<String, String>();
      ByteArrayInputStream buffer = new ByteArrayInputStream(array, offset, length);
      DataInputStream input = new DataInputStream(buffer);
      String process = input.readUTF();
      String thread = input.readUTF();
      String instruction = input.readUTF();
      String resource = input.readUTF();
      int line = input.readInt();
      int depth = input.readInt();
      int key = input.readInt();
      int count = input.readInt();
      
      for(int i = 0; i < count; i++) {
         String name = input.readUTF();
         String value = input.readUTF();
         
         variables.put(name, value);
      }
      return new ScopeEvent(process, thread, instruction, resource, line, depth, key, variables);
   }

   @Override
   public MessageEnvelope toMessage(ScopeEvent event) throws IOException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      DataOutputStream output = new DataOutputStream(buffer);
      Map<String, String> variables = event.getVariables();
      Set<String> names = variables.keySet();
      String process = event.getProcess();
      String thread = event.getThread();
      String instruction = event.getInstruction();
      String resource = event.getResource();
      int line = event.getLine();
      int depth = event.getDepth();
      int key = event.getKey();
      int count = variables.size();
      
      output.writeUTF(process);
      output.writeUTF(thread);
      output.writeUTF(instruction);
      output.writeUTF(resource);
      output.writeInt(line);
      output.writeInt(depth);
      output.writeInt(key);
      output.writeInt(count);
      
      for(String name : names) {
         String value = variables.get(name);
         
         output.writeUTF(name);
         output.writeUTF(value);
      }
      output.flush();
      
      byte[] array = buffer.toByteArray();
      return new MessageEnvelope(process, SCOPE.code, array, 0, array.length);
   }
}
