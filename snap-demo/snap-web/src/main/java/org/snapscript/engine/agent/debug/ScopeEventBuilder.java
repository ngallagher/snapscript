package org.snapscript.engine.agent.debug;

import static org.snapscript.engine.event.ScopeEvent.RUNNING;
import static org.snapscript.engine.event.ScopeEvent.SUSPENDED;

import java.util.Collections;
import java.util.Map;

import org.snapscript.engine.event.ScopeEvent;

public class ScopeEventBuilder {

   private final ScopeExtractor extractor;
   private final Class instruction;
   private final String process;
   private final String resource;
   private final String thread;
   private final int line;
   private final int depth;
   private final int count;
   
   public ScopeEventBuilder(ScopeExtractor extractor, String process, String thread, Class instruction, String resource, int line, int depth, int count) {
      this.instruction = instruction;
      this.extractor = extractor;
      this.process = process;
      this.thread = thread;
      this.resource = resource;
      this.line = line;
      this.depth = depth;
      this.count = count;
   }
   
   public ScopeEvent suspendEvent() {
      Map<String, String> variables = extractor.build();
      String name = instruction.getSimpleName();
      
      return new ScopeEvent(process, thread, name, SUSPENDED, resource, line, depth, count, variables);
   }
   
   public ScopeEvent resumeEvent() {
      Map<String, String> variables = Collections.emptyMap();
      String name = instruction.getSimpleName();
      
      return new ScopeEvent(process, thread, name, RUNNING, resource, line, depth, count, variables);
   }
}
