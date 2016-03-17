package org.snapscript.develop.command;

public class ProblemCommand implements Command {

   private String project;
   private String description;
   private String resource;
   private int line;
   
   public ProblemCommand() {
      super();
   }
   
   public ProblemCommand(String project, String description, String resource, int line) {
      this.description = description;
      this.resource = resource;
      this.project = project;
      this.line = line;
   }

   public String getProject() {
      return project;
   }

   public void setProject(String project) {
      this.project = project;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getResource() {
      return resource;
   }

   public void setResource(String resource) {
      this.resource = resource;
   }

   public int getLine() {
      return line;
   }

   public void setLine(int line) {
      this.line = line;
   }
} 
