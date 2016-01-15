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
   
   public Map<String, Map<Integer, Boolean>> getBreakpoints() {
      return breakpoints;
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
