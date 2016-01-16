package org.snapscript.engine.event;

public class ResumeEvent implements ProcessEvent {

   private String process;
   private String thread;
   
   public ResumeEvent(String process, String thread) {
      this.process = process;
      this.thread = thread;
   }
   
   @Override
   public String getProcess() {
      return process;
   }
   
   public String getThread() {
      return thread;
   }
}
