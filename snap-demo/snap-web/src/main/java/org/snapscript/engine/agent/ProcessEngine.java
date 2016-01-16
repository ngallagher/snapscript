package org.snapscript.engine.agent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.snapscript.engine.command.ExecuteCommand;
import org.snapscript.engine.command.ResumeCommand;
import org.snapscript.engine.command.SuspendCommand;
import org.snapscript.engine.event.ProcessEventListener;

public class ProcessEngine {
   
   private final Map<String, ProcessAgentConnection> connections;
   private final ProcessAgentPool pool;

   public ProcessEngine(String root, int port, int capacity) throws Exception {
      this.connections = new ConcurrentHashMap<String, ProcessAgentConnection>();
      this.pool = new ProcessAgentPool(root, port, capacity);
   }
   
   public void execute(ProcessEventListener listener, ExecuteCommand command, String client) {
      String system = System.getProperty("os.name");
      ProcessAgentConnection connection = pool.acquire(listener, system);
      ProcessAgentConnection current = connections.remove(client);
      
      if(current != null) {
         current.close();
      }
      if(connection != null) {
         Map<String, Map<Integer, Boolean>> breakpoints = command.getBreakpoints();
         String project = command.getProject();
         String resource = command.getResource();
         
         connections.put(client, connection);
         connection.execute(project, resource, breakpoints);
      }
   }
   
   public void suspend(SuspendCommand command, String client) {
      ProcessAgentConnection connection = connections.get(client);
      
      if(connection != null) {
         Map<String, Map<Integer, Boolean>> breakpoints = command.getBreakpoints();
         connection.suspend(breakpoints);
      }
   }
   
   public void resume(ResumeCommand command, String client) {
      ProcessAgentConnection connection = connections.get(client);
      
      if(connection != null) {
         String thread = command.getThread();
         connection.resume(thread);
      }
   }
   
   public void stop(String client) {
      ProcessAgentConnection connection = connections.remove(client);
      
      if(connection != null) {
         connection.close();
      }
   }
   
   public void start() {
      pool.start();
   }

}
