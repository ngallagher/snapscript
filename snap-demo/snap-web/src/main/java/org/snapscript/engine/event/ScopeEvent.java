package org.snapscript.engine.event;

import java.util.Map;

public class ScopeEvent implements ProcessEvent {

   private Map<String, String> variables;
   private String instruction;
   private String process;
   private String resource;
   private String thread;
   private int line;
   private int key;
   
   public ScopeEvent(String process, String thread, String instruction, String resource, int line, int key, Map<String, String> variables) {
      this.variables = variables;
      this.instruction = instruction;
      this.resource = resource;
      this.process = process;
      this.thread = thread;
      this.line = line;
      this.key = key;
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

   public String getThread() {
      return thread;
   }

   public int getLine() {
      return line;
   }
   
   public int getKey() {
      return key;
   }
}
