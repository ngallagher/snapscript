package org.snapscript.agent.event;

import static org.snapscript.agent.event.ProcessEventType.BROWSE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BrowseEventMarshaller implements ProcessEventMarshaller<BrowseEvent>{

   @Override
   public BrowseEvent fromMessage(MessageEnvelope message) throws IOException {
      byte[] array = message.getData();
      int length = message.getLength();
      int offset = message.getOffset();
      Set<String> expand = new HashSet<String>();
      ByteArrayInputStream buffer = new ByteArrayInputStream(array, offset, length);
      DataInputStream input = new DataInputStream(buffer);
      String process = input.readUTF();
      String thread = input.readUTF();
      int count = input.readInt();
      
      for(int i = 0; i < count; i++) {
         String path = input.readUTF();
         expand.add(path);
      }
      return new BrowseEvent(process, thread, expand);
   }

   @Override
   public MessageEnvelope toMessage(BrowseEvent event) throws IOException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      DataOutputStream output = new DataOutputStream(buffer);
      Set<String> expand = event.getExpand();
      String process = event.getProcess();
      String thread = event.getThread();
      int count = expand.size();
      
      output.writeUTF(process);
      output.writeUTF(thread);
      output.writeInt(count);
      
      for(String name : expand) {
         output.writeUTF(name);
      }
      output.flush();
      
      byte[] array = buffer.toByteArray();
      return new MessageEnvelope(process, BROWSE.code, array, 0, array.length);
   }

}
