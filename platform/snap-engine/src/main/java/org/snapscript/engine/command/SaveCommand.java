package org.snapscript.engine.command;

public class SaveCommand implements Command {

   private String resource;
   private String project;
   private String source;
   private boolean directory;
   
   public SaveCommand() {
      super();
   }
   
   public SaveCommand(String project, String resource, String source, boolean directory) {
      this.project = project;
      this.resource = resource;
      this.source = source;
      this.directory = directory;
   }

   public String getResource() {
      return resource;
   }

   public void setResource(String resource) {
      this.resource = resource;
   }

   public String getProject() {
      return project;
   }

   public void setProject(String project) {
      this.project = project;
   }

   public String getSource() {
      return source;
   }

   public void setSource(String source) {
      this.source = source;
   }

   public boolean isDirectory() {
      return directory;
   }

   public void setDirectory(boolean directory) {
      this.directory = directory;
   }

}
