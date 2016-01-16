package org.snapscript.engine.command;

import java.util.Map;

public class ScopeCommand implements Command {
   
   private Map<String, String> variables;
   private String resource;
   private String thread;
   private int line;
   
   public ScopeCommand() {
      super();
   }

   public ScopeCommand(String thread, String resource, int line, Map<String, String> variables) {
      this.variables = variables;
      this.thread = thread;
      this.resource = resource;
      this.line = line;
   }

   public Map<String, String> getVariables() {
      return variables;
   }

   public void setVariables(Map<String, String> variables) {
      this.variables = variables;
   }

   public String getResource() {
      return resource;
   }

   public void setResource(String resource) {
      this.resource = resource;
   }

   public String getThread() {
      return thread;
   }

   public void setThread(String thread) {
      this.thread = thread;
   }

   public int getLine() {
      return line;
   }

   public void setLine(int line) {
      this.line = line;
   }
}
