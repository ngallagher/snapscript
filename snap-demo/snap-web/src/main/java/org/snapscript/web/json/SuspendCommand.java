package org.snapscript.web.json;

import java.util.HashMap;
import java.util.Map;

public class SuspendCommand {

   private Map<String, Map<Integer, Boolean>> breakpoints;
   private String project;
   
   public SuspendCommand() {
      this.breakpoints = new HashMap<String, Map<Integer, Boolean>>();
   }
   
   public Map<String, Map<Integer, Boolean>> getBreakpoints() {
      return breakpoints;
   }
   
   public String getProject() {
      return project;
   }
}
