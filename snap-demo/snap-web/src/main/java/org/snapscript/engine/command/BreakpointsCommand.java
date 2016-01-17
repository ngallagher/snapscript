package org.snapscript.engine.command;

import java.util.Map;

public class BreakpointsCommand implements Command {

   private Map<String, Map<Integer, Boolean>> breakpoints;
   private String project;
   
   public BreakpointsCommand() {
      super();
   }
   
   public BreakpointsCommand(String project, Map<String, Map<Integer, Boolean>> breakpoints) {
      this.breakpoints = breakpoints;
      this.project = project;
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
}
