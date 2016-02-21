package org.snapscript.agent.event;

public class PongEvent implements ProcessEvent {

   private String process;
   private String resource;
   private boolean running;
   
   public PongEvent(String process) {
      this(process, null, false);
   }
   
   public PongEvent(String process, String resource, boolean running) {
      this.resource = resource;
      this.process = process;
      this.running = running;
   }
   
   @Override
   public String getProcess() {
      return process;
   }
   
   public String getResource() {
      return resource;
   }
   
   public boolean isRunning() {
      return running;
   }
}
