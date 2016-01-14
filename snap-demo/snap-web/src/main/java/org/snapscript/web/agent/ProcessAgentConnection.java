package org.snapscript.web.agent;

import java.util.Collections;

import org.snapscript.web.binary.event.ExecuteEvent;
import org.snapscript.web.binary.event.PingEvent;
import org.snapscript.web.binary.event.ProcessEventChannel;

public class ProcessAgentConnection {

   private final ProcessEventChannel channel;
   private final String process;

   public ProcessAgentConnection(ProcessEventChannel channel, String process) {
      this.channel = channel;
      this.process = process;
   }

   public void execute(String project, String path) {
      try {
         ExecuteEvent event = new ExecuteEvent(process, project, path, Collections.EMPTY_MAP);
         channel.send(event);
      } catch (Exception e) {
         e.printStackTrace();
         try {
            channel.close();
         } catch (Exception ex) {
            ex.printStackTrace();
         }
         throw new IllegalStateException("Could not execute script '" + path + "' for '" + process + "'", e);
      }
   }

   public boolean ping() {
      try {
         PingEvent event = new PingEvent(process);
         channel.send(event);
         return true;
      } catch (Exception e) {
         e.printStackTrace();
         try {
            channel.close();
         } catch (Exception ex) {
            ex.printStackTrace();
         }
      }
      return false;
   }
   
   public void close() {
      try {
         channel.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   @Override
   public String toString() {
      return process;
   }
}
