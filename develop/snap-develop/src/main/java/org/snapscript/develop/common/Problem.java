package org.snapscript.develop.common;

public class Problem {

   private final String resource;
   private final String project;
   private final int line;
   
   public Problem(String project, String resource, int line) {
      this.resource = resource;
      this.project = project;
      this.line = line;
   }
   
   public String getProject() {
      return project;
   }
   
   public String getResource() {
      return resource;
   }
   
   public int getLine() {
      return line;
   }
}
