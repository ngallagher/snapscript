package org.snapscript.engine.agent;

import java.util.Map;

import org.snapscript.engine.command.ExecuteCommand;
import org.snapscript.engine.event.ProcessEventListener;

public class ProcessEngine {
   
   private final ProcessAgentPool pool;

   public ProcessEngine(String root, int port, int capacity) throws Exception {
      this.pool = new ProcessAgentPool(root, port, capacity);
   }
   
   public void execute(ProcessEventListener listener, ExecuteCommand command) {
      Map<String, Map<Integer, Boolean>> breakpoints = command.getBreakpoints();
      String project = command.getProject();
      String resource = command.getResource();
      String system = System.getProperty("os.name"); // XXX change this
      ProcessAgentConnection connection = pool.acquire(listener, system);
      connection.execute(project, resource, breakpoints);
   }
   
   public void start() {
      pool.start();
   }

}
