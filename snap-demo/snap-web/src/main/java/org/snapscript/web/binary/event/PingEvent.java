package org.snapscript.web.binary.event;

public class PingEvent implements ProcessEvent {

   private String process;
   
   public PingEvent(String process) {
      this.process = process;
   }
   
   @Override
   public String getProcess() {
      return process;
   }
}
