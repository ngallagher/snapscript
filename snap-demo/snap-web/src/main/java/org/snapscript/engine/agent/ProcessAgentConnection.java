package org.snapscript.engine.agent;

import java.util.Map;

import org.snapscript.engine.event.BreakpointsEvent;
import org.snapscript.engine.event.ExecuteEvent;
import org.snapscript.engine.event.PingEvent;
import org.snapscript.engine.event.ProcessEventChannel;
import org.snapscript.engine.event.StepEvent;

public class ProcessAgentConnection {

   private final ProcessEventChannel channel;
   private final String process;

   public ProcessAgentConnection(ProcessEventChannel channel, String process) {
      this.channel = channel;
      this.process = process;
   }

   public boolean execute(String project, String path, Map<String, Map<Integer, Boolean>> breakpoints) {
      try {
         ExecuteEvent event = new ExecuteEvent(process, project, path, breakpoints);
         return channel.send(event);
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
   
   public boolean suspend(Map<String, Map<Integer, Boolean>> breakpoints) {
      try {
         BreakpointsEvent event = new BreakpointsEvent(process, breakpoints);
         return channel.send(event);
      } catch (Exception e) {
         e.printStackTrace();
         try {
            channel.close();
         } catch (Exception ex) {
            ex.printStackTrace();
         }
         throw new IllegalStateException("Could not set breakpoints '" + breakpoints + "' for '" + process + "'", e);
      }
   }
   
   public boolean step(String thread, int type) {
      try {
         StepEvent event = new StepEvent(process, thread, type);
         return channel.send(event);
      } catch (Exception e) {
         e.printStackTrace();
         try {
            channel.close();
         } catch (Exception ex) {
            ex.printStackTrace();
         }
         throw new IllegalStateException("Could not resume script thread '" + thread + "' for '" + process + "'", e);
      }
   }

   public boolean ping() {
      try {
         PingEvent event = new PingEvent(process);
         return channel.send(event);
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
