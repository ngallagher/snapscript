package org.snapscript.agent.event;

import java.util.Map;

public class ScopeEvent implements ProcessEvent {
   
   public static final String SUSPENDED = "SUSPENDED";
   public static final String RUNNING = "RUNNING";

   private Map<String, Map<String, String>> variables;
   private String instruction;
   private String status;
   private String process;
   private String resource;
   private String thread;
   private int sequence;
   private int line;
   private int depth;
   
   public ScopeEvent(String process, String thread, String instruction, String status, String resource, int line, int depth, int sequence, Map<String, Map<String, String>> variables) {
      this.variables = variables;
      this.instruction = instruction;
      this.sequence = sequence;
      this.resource = resource;
      this.process = process;
      this.thread = thread;
      this.status = status;
      this.depth = depth;
      this.line = line;
   }
   
   @Override
   public String getProcess() {
      return process;
   }

   public Map<String, Map<String, String>> getVariables() {
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
   
   public int getSequence() {
      return sequence;
   }
}