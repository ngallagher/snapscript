package org.snapscript.engine;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.snapscript.agent.ProcessAgentConnection;
import org.snapscript.agent.ProcessAgentPool;
import org.snapscript.agent.event.ProcessEventFilter;
import org.snapscript.agent.event.ProcessEventListener;
import org.snapscript.agent.event.StepEvent;
import org.snapscript.engine.command.BreakpointsCommand;
import org.snapscript.engine.command.BrowseCommand;
import org.snapscript.engine.command.ExecuteCommand;
import org.snapscript.engine.command.StepCommand;

public class ProcessEngine {
   
   private final Map<String, ProcessAgentConnection> connections; // active processes
   private final ProcessAgentPool pool;

   public ProcessEngine(int port, int capacity) throws Exception {
      this.connections = new ConcurrentHashMap<String, ProcessAgentConnection>();
      this.pool = new ProcessAgentPool(port, capacity);
   }
   
   public void register(ProcessEventListener listener) {
      pool.register(listener);
   }
   
   public void remove(ProcessEventListener listener) {
      pool.remove(listener);
   }
   
   public boolean execute(ExecuteCommand command) {
      return execute(command, null);
   }
   
   public boolean execute(ExecuteCommand command, ProcessEventFilter filter) { 
      String system = System.getProperty("os.name");
      ProcessAgentConnection connection = pool.acquire(system);
      
      if(connection != null) {
         Map<String, Map<Integer, Boolean>> breakpoints = command.getBreakpoints();
         String project = command.getProject();
         String resource = command.getResource();
         String process = connection.toString();
         
         if(filter != null) {
            filter.update(process);
         }
         connections.put(process, connection);
         
         return connection.execute(project, resource, breakpoints);
      }
      return true;
   }
   
   public boolean breakpoints(BreakpointsCommand command, String process) {
      ProcessAgentConnection connection = connections.get(process);
      
      if(connection != null) {
         Map<String, Map<Integer, Boolean>> breakpoints = command.getBreakpoints();
         return connection.suspend(breakpoints);
      }
      return true;
   }
   
   public boolean browse(BrowseCommand command, String process) {
      ProcessAgentConnection connection = connections.get(process);
      
      if(connection != null) {
         Set<String> expand = command.getExpand();
         String thread = command.getThread();
         return connection.browse(thread, expand);
      }
      return true;
   }
   
   public boolean step(StepCommand command, String process) {
      ProcessAgentConnection connection = connections.get(process);
      
      if(connection != null) {
         String thread = command.getThread();
         
         if(command.isRun()) {
            return connection.step(thread, StepEvent.RUN);
         } else if(command.isStepIn()) {
            return connection.step(thread, StepEvent.STEP_IN);
         } else if(command.isStepOut()) {
            return connection.step(thread, StepEvent.STEP_OUT);
         } else if(command.isStepOver()) {
            return connection.step(thread, StepEvent.STEP_OVER);
         }
      }
      return true;
   }
   
   public boolean stop(String process) {
      ProcessAgentConnection connection = connections.remove(process);
      
      if(connection != null) {
         connection.close();
      }
      return true;
   }
   
   public boolean ping(String process) {
      ProcessAgentConnection connection = connections.get(process);
      
      if(connection != null) {
         return connection.ping();
      }
      return true;
   }
   
   public void start(String address) {
      pool.start(address);
   }
   
   public void launch() {
      pool.launch();
   }

}
