package org.snapscript.engine.command;

public class AttachCommand implements Command {

   private String process;
   private boolean focus;
   
   public AttachCommand() {
      super();
   }
   
   public AttachCommand(String process, boolean focus) {
      this.process = process;
      this.focus = focus;
   }

   public String getProcess() {
      return process;
   }

   public void setProcess(String process) {
      this.process = process;
   }

   public boolean isFocus() {
      return focus;
   }

   public void setFocus(boolean focus) {
      this.focus = focus;
   }
   
   
}