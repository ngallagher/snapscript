package org.snapscript.engine.command;

import java.util.Map;

public class ScopeCommand implements Command {
   
   private Map<String, String> variables;
   private String instruction;
   private String resource;
   private String status;
   private String thread;
   private int depth;
   private int line;
   
   public ScopeCommand() {
      super();
   }

   public ScopeCommand(String thread, String instruction, String status, String resource, int line, int depth, Map<String, String> variables) {
      this.variables = variables;
      this.instruction = instruction;
      this.thread = thread;
      this.resource = resource;
      this.status = status;
      this.depth = depth;
      this.line = line;
   }

   public Map<String, String> getVariables() {
      return variables;
   }

   public void setVariables(Map<String, String> variables) {
      this.variables = variables;
   }

   public String getInstruction() {
      return instruction;
   }

   public void setInstruction(String instruction) {
      this.instruction = instruction;
   }
  
   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
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

   public int getDepth() {
      return depth;
   }

   public void setDepth(int depth) {
      this.depth = depth;
   }

   public int getLine() {
      return line;
   }

   public void setLine(int line) {
      this.line = line;
   }
}
