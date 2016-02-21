package org.snapscript.engine.command;

public class StatusCommand implements Command {

   private String resource;
   private String process;
   private boolean focus;
   
   public StatusCommand() {
      super();
   }
   
   public StatusCommand(String process, String resource, boolean focus) {
      this.process = process;
      this.resource = resource;
      this.focus = focus;
   }

   public String getProcess() {
      return process;
   }

   public void setProcess(String process) {
      this.process = process;
   }

   public String getResource() {
      return resource;
   }

   public void setResource(String resource) {
      this.resource = resource;
   }

   public boolean isFocus() {
      return focus;
   }

   public void setFocus(boolean focus) {
      this.focus = focus;
   }
   
   
}