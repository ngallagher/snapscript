package org.snapscript.web.binary.event;

import static org.snapscript.web.binary.event.ProcessEventType.EXECUTE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.snapscript.web.binary.BinaryMessage;

public class ExecuteEventMarshaller implements ProcessEventMarshaller<ExecuteEvent> {

   @Override
   public ExecuteEvent fromMessage(BinaryMessage message) throws IOException {
      byte[] array = message.getData();
      int length = message.getLength();
      int offset = message.getOffset();
      ByteArrayInputStream buffer = new ByteArrayInputStream(array, offset, length);
      DataInputStream input = new DataInputStream(buffer);
      Map<String, Map<Integer, Boolean>> breakpoints = new HashMap<String, Map<Integer, Boolean>>();
      String process = input.readUTF();
      String project = input.readUTF();
      String resource = input.readUTF();
      int breakpointSize = input.readInt();
      
      for(int i = 0; i < breakpointSize; i++) {
         Map<Integer, Boolean> locations = new HashMap<Integer, Boolean>();
         String script = input.readUTF();
         int locationSize = input.readInt();

         for(int j = 0; j < locationSize; j++) {
            int line = input.readInt();
            boolean enable = input.readBoolean();
            
            locations.put(line, enable);
         }
         breakpoints.put(script, locations);
      }
      return new ExecuteEvent(process, project, resource, breakpoints);
   }

   @Override
   public BinaryMessage toMessage(ExecuteEvent value) throws IOException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      DataOutputStream output = new DataOutputStream(buffer);
      Map<String, Map<Integer, Boolean>> breakpoints = value.getBreakpoints();
      Set<String> scripts = breakpoints.keySet();
      String process = value.getProcess();
      String resource = value.getResource();
      String project = value.getProject();
      int breakpointSize = breakpoints.size();
      
      output.writeUTF(process);
      output.writeUTF(project);
      output.writeUTF(resource);
      output.writeInt(breakpointSize);
      
      for(String script : scripts) {
         Map<Integer, Boolean> locations = breakpoints.get(script);
         Set<Integer> lines = locations.keySet();
         int locationSize = locations.size();
         
         output.writeUTF(script);
         output.writeInt(locationSize);
         
         for(Integer line : lines) {
            Boolean enable = locations.get(line);
            
            output.writeInt(line);
            output.writeBoolean(enable);
         }
      }
      output.flush();
      byte[] array = buffer.toByteArray();
      return new BinaryMessage(process, EXECUTE.code, array, 0, array.length);
   }

}