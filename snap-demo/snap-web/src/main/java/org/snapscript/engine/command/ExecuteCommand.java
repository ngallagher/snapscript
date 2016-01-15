package org.snapscript.engine.command;

import java.util.HashMap;
import java.util.Map;

public class ExecuteCommand implements Command {

   private Map<String, Map<Integer, Boolean>> breakpoints;
   private String project;
   private String resource;
   private String source;
   
   public ExecuteCommand() {
      this.breakpoints = new HashMap<String, Map<Integer, Boolean>>();
   }
   
   public ExecuteCommand(String project, String resource, String source, Map<String, Map<Integer, Boolean>> breakpoints) {
      this.breakpoints = breakpoints;
      this.resource = resource;
      this.project = project;
      this.source = source;
   }

   public Map<String, Map<Integer, Boolean>> getBreakpoints() {
      return breakpoints;
   }

   public void setBreakpoints(Map<String, Map<Integer, Boolean>> breakpoints) {
      this.breakpoints = breakpoints;
   }

   public String getProject() {
      return project;
   }

   public void setProject(String project) {
      this.project = project;
   }

   public String getResource() {
      return resource;
   }

   public void setResource(String resource) {
      this.resource = resource;
   }

   public String getSource() {
      return source;
   }

   public void setSource(String source) {
      this.source = source;
   }
}
