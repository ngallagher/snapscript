package org.snapscript.web.json;

import java.util.HashMap;
import java.util.Map;

public class ExecuteCommand {

   private Map<String, Map<Integer, Boolean>> breakpoints;
   private String project;
   private String source;
   
   public ExecuteCommand() {
      this.breakpoints = new HashMap<String, Map<Integer, Boolean>>();
   }
   
   public Map<String, Map<Integer, Boolean>> getBreakpoints() {
      return breakpoints;
   }
   
   public String getSource() {
      return source;
   }
   
   public String getProject() {
      return project;
   }
}
