package org.snapscript.web.binary.event;

public class PongEvent implements ProcessEvent {

   private String process;
   
   public PongEvent(String process) {
      this.process = process;
   }
   
   @Override
   public String getProcess() {
      return process;
   }
}
