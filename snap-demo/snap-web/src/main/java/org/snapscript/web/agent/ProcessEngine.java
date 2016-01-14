package org.snapscript.web.agent;

import org.snapscript.web.binary.event.ProcessEventListener;

public class ProcessEngine {
   
   private static final String RESOURCE_URL = "http://localhost:%s/resource";

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
