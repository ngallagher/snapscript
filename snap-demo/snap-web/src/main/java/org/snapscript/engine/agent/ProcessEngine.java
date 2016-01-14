package org.snapscript.engine.agent;

import org.snapscript.engine.event.ProcessEventListener;

public class ProcessEngine {
   
   private final ProcessAgentPool pool;

   public ProcessEngine(String root, int port, int capacity) throws Exception {
      this.pool = new ProcessAgentPool(root, port, capacity);
   }
   
   public void execute(ProcessEventListener listener, String project, String path, String system) {
      ProcessAgentConnection connection = pool.acquire(listener, system);
      connection.execute(project, path);
   }
   
   public void start() {
      pool.start();
   }

}
