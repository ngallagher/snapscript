package org.snapscript.engine.agent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.snapscript.engine.command.BreakpointsCommand;
import org.snapscript.engine.command.ExecuteCommand;
import org.snapscript.engine.command.StepCommand;
import org.snapscript.engine.event.ProcessEventListener;
import org.snapscript.engine.event.StepEvent;

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
   
   public void breakpoints(BreakpointsCommand command, String client) {
      ProcessAgentConnection connection = connections.get(client);
      
      if(connection != null) {
         Map<String, Map<Integer, Boolean>> breakpoints = command.getBreakpoints();
         connection.suspend(breakpoints);
      }
   }
   
   public void step(StepCommand command, String client) {
      ProcessAgentConnection connection = connections.get(client);
      
      if(connection != null) {
         String thread = command.getThread();
         
         if(command.isRun()) {
            connection.step(thread, StepEvent.RUN);
         } else if(command.isStepIn()) {
            connection.step(thread, StepEvent.STEP_IN);
         } else if(command.isStepOut()) {
            connection.step(thread, StepEvent.STEP_OUT);
         } else if(command.isStepOver()) {
            connection.step(thread, StepEvent.STEP_OVER);
         }
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
