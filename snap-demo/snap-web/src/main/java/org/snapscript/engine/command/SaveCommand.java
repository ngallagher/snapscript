package org.snapscript.engine.command;

public class SaveCommand {

   private String resource;
   private String project;
   private String source;
   
   public SaveCommand() {
      super();
   }
   
   public String getResource() {
      return resource;
   }
   
   public String getSource() {
      return source;
   }
   
   public String getProject() {
      return project;
   }
}
