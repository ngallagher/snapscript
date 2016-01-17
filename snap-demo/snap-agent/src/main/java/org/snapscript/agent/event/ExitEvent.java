package org.snapscript.agent.event;

public class ExitEvent implements ProcessEvent {

   private String process;
   
   public ExitEvent(String process) {
      this.process = process;
   }
   
   @Override
   public String getProcess() {
      return process;
   }
}