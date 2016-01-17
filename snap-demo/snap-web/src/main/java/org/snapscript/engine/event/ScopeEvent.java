package org.snapscript.engine.event;

import java.util.Map;

public class ScopeEvent implements ProcessEvent {
   
   public static final String SUSPENDED = "SUSPENDED";
   public static final String RUNNING = "RUNNING";

   private Map<String, String> variables;
   private String instruction;
   private String status;
   private String process;
   private String resource;
   private String thread;
   private int line;
   private int depth;
   private int count;
   
   public ScopeEvent(String process, String thread, String instruction, String status, String resource, int line, int depth, int count, Map<String, String> variables) {
      this.variables = variables;
      this.instruction = instruction;
      this.resource = resource;
      this.process = process;
      this.thread = thread;
      this.status = status;
      this.depth = depth;
      this.line = line;
      this.count = count;
   }
   
   @Override
   public String getProcess() {
      return process;
   }

   public Map<String, String> getVariables() {
      return variables;
   }

   public String getInstruction() {
      return instruction;
   }
   
   public String getResource() {
      return resource;
   }

   public String getStatus() {
      return status;
   }

   public String getThread() {
      return thread;
   }
   
   public int getDepth() {
      return depth;
   }

   public int getLine() {
      return line;
   }
   
   public int getCount() {
      return count;
   }
}
