package org.snapscript.engine.command;

public class StartCommand implements Command {

   private String process;
   private String resource;
   
   public StartCommand(){
      super();
   }
   
   public StartCommand(String process, String resource) {
      this.process = process;
      this.resource = resource;
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
}
