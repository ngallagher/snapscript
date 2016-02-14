package org.snapscript.agent;

import java.io.IOException;
import java.io.OutputStream;

import org.snapscript.agent.event.ProcessEventChannel;
import org.snapscript.agent.event.ProcessEventType;
import org.snapscript.agent.event.WriteErrorEvent;
import org.snapscript.agent.event.WriteOutputEvent;

public class ProcessAgentStream extends OutputStream {

   private final ProcessEventChannel channel;
   private final ProcessEventType type;
   private final String process;
   
   public ProcessAgentStream(ProcessEventType type, ProcessEventChannel channel, String process) {
      this.process = process;
      this.channel = channel;
      this.type = type;
   }
   
   public void write(int octet) throws IOException {
      write(new byte[]{(byte)octet});
   }
   
   public void write(byte[] octets) throws IOException {
      write(octets, 0, octets.length);
   }
   
   public void write(byte[] octets, int offset, int length) throws IOException {
      try {
         if(type == ProcessEventType.WRITE_ERROR) {
            WriteErrorEvent event = new WriteErrorEvent(process, octets, offset, length);
            channel.send(event);
         } else {
            WriteOutputEvent event = new WriteOutputEvent(process, octets, offset, length);
            channel.send(event);
         }
      }catch(Exception e) {
         throw new IOException("Error sending write event", e);
      }
   }
   
}