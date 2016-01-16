package org.snapscript.engine.command;

public class ResumeCommand implements Command {
   
   private String thread;
   
   public ResumeCommand() {
      super();
   }
   
   public ResumeCommand(String thread) {
      this.thread = thread;
   }

   public String getThread() {
      return thread;
   }

   public void setThread(String thread) {
      this.thread = thread;
   }
}
