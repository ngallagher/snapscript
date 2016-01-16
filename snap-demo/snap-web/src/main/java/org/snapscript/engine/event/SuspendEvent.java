package org.snapscript.engine.event;

import java.util.Map;

public class SuspendEvent implements ProcessEvent {

   private Map<String, Map<Integer, Boolean>> breakpoints;
   private String process;
   
   public SuspendEvent(String process, Map<String, Map<Integer, Boolean>> breakpoints) {
      this.breakpoints = breakpoints;
      this.process = process;
   }
   
   @Override
   public String getProcess() {
      return process;
   }
   
   public Map<String, Map<Integer, Boolean>> getBreakpoints() {
      return breakpoints;
   }
}