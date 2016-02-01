package org.snapscript.agent;

import java.io.PrintStream;
import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

import org.snapscript.agent.common.ExceptionBuilder;
import org.snapscript.agent.debug.BreakpointMatcher;
import org.snapscript.agent.debug.ResumeType;
import org.snapscript.agent.debug.SuspendController;
import org.snapscript.agent.debug.SuspendInterceptor;
import org.snapscript.agent.event.BeginEvent;
import org.snapscript.agent.event.BreakpointsEvent;
import org.snapscript.agent.event.BrowseEvent;
import org.snapscript.agent.event.ExecuteEvent;
import org.snapscript.agent.event.ExitEvent;
import org.snapscript.agent.event.PingEvent;
import org.snapscript.agent.event.PongEvent;
import org.snapscript.agent.event.ProcessEventAdapter;
import org.snapscript.agent.event.ProcessEventChannel;
import org.snapscript.agent.event.ProcessEventType;
import org.snapscript.agent.event.ProfileEvent;
import org.snapscript.agent.event.RegisterEvent;
import org.snapscript.agent.event.StepEvent;
import org.snapscript.agent.event.socket.SocketEventClient;
import org.snapscript.agent.profiler.ExecutionProfiler;
import org.snapscript.agent.profiler.ProfileResult;
import org.snapscript.agent.profiler.ProfileResultUpdater;
import org.snapscript.compile.Executable;
import org.snapscript.compile.ResourceCompiler;
import org.snapscript.compile.StoreContext;
import org.snapscript.core.EmptyModel;
import org.snapscript.core.Model;
import org.snapscript.core.ModelScope;
import org.snapscript.core.Module;
import org.snapscript.core.Package;
import org.snapscript.core.PackageLinker;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.store.RemoteStore;

public class ProcessAgent {

   private static final String SOURCE =
   "class InternalTypeForScriptAgent {\n"+
   "   static const ARR = [\"a\",\"b\",\"c\"];\n"+
   "   var x;\n"+
   "   new(index){\n"+
   "      this.x=ARR[index];\n"+
   "   }\n"+
   "   dump(){\n"+
   "      System.err.println(x);\n"+
   "   }\n"+
   "}\n"+
   "var privateVariableInScriptAgent = new InternalTypeForScriptAgent(1);\n"+
   "privateVariableInScriptAgent.dump();\n"+
   "System.err.println(privateVariableInScriptAgent.x);\n"+
   "System.err.println(InternalTypeForScriptAgent.ARR);";

   private final SuspendController controller;
   private final ProcessAgentStore store;
   private final StoreContext context;
   private final ResourceCompiler compiler;
   private final ExecutionProfiler profiler;
   private final BreakpointMatcher matcher;
   private final RemoteStore remoteReader;
   private final Model model;
   private final String process;
   private final int port;

   public ProcessAgent(URI rootURI, String process, int port) {
      this.remoteReader = new RemoteStore(rootURI);
      this.store = new ProcessAgentStore(remoteReader);
      this.context = new StoreContext(store);
      this.compiler = new ResourceCompiler(context);
      this.controller = new SuspendController();
      this.matcher = new BreakpointMatcher();
      this.profiler = new ExecutionProfiler();
      this.model = new EmptyModel();
      this.process = process;
      this.port = port;
   }
   
   public void run() throws Exception {
      try {
         System.err.println(SOURCE);
         long start = System.currentTimeMillis();
         PackageLinker linker = context.getLinker();
         Package library = linker.link("moduleForTheScriptAgent", SOURCE, "script");
         Module module = context.getBuilder().create("moduleForTheScriptAgent");
         Scope scope = new ModelScope(model, module);
         Statement script = library.compile(scope); 
         long middle = System.currentTimeMillis();
         script.execute(scope);  // warm up the agent for quicker execution
         long finish = System.currentTimeMillis();
         System.err.println("Compile time="+(middle-start));
         System.err.println("Execute time="+(finish-middle));
      }catch(Exception e) {
         e.printStackTrace();
      }
      TraceAnalyzer analyzer = context.getAnalyzer();
      try {
         String system = System.getProperty("os.name");
         RegisterEvent register = new RegisterEvent(process, system);
         ClientListener listener = new ClientListener(process);
         SocketEventClient client = new SocketEventClient(listener);
         ProcessEventChannel channel = client.connect(port);
         SuspendInterceptor interceptor = new SuspendInterceptor(channel, matcher, controller, process);
         
         ///analyzer.register(profiler);
         analyzer.register(interceptor);
         channel.send(register); // send the initial register event
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   private class ClientListener extends ProcessEventAdapter {
      
      private final String process;
      
      public ClientListener(String process) throws Exception {
         this.process = process;
      }

      @Override
      public void onExecute(ProcessEventChannel channel, ExecuteEvent event) throws Exception {
         Map<String, Map<Integer, Boolean>> breakpoints = event.getBreakpoints();
         String resource = event.getResource();
         String process = event.getProcess();
         String project = event.getProject();
         matcher.update(breakpoints);
         store.update(project); // XXX rubbish
         ExecuteTask task = new ExecuteTask(channel, process, project, resource);
         task.start();
      }
      
      @Override
      public void onBreakpoints(ProcessEventChannel channel, BreakpointsEvent event) throws Exception {
         Map<String, Map<Integer, Boolean>> breakpoints = event.getBreakpoints();
         matcher.update(breakpoints);
      }
      
      @Override
      public void onStep(ProcessEventChannel channel, StepEvent event) throws Exception {
         String thread = event.getThread();
         int type = event.getType();
         
         if(type == StepEvent.RUN) {
            controller.resume(ResumeType.RUN, thread);
         } else if(type == StepEvent.STEP_IN) {
            controller.resume(ResumeType.STEP_IN, thread);
         } else if(type == StepEvent.STEP_OUT) {
            controller.resume(ResumeType.STEP_OUT, thread);
         } else if(type == StepEvent.STEP_OVER) {
            controller.resume(ResumeType.STEP_OVER, thread);
         }
      }
      
      @Override
      public void onBrowse(ProcessEventChannel channel, BrowseEvent event) throws Exception {
         String thread = event.getThread();
         Set<String> expand = event.getExpand();
         
         controller.browse(expand, thread);
      }

      @Override
      public void onPing(ProcessEventChannel channel, PingEvent event) throws Exception {
         PongEvent pong = new PongEvent(process);
         channel.send(pong);
      }

      @Override
      public void onClose(ProcessEventChannel channel) throws Exception {
         System.exit(0);
      }
   }
   
   private class ExecuteTask extends Thread {
      
      private final ProcessEventChannel client;
      private final String resource;
      private final String process;
      private final String project;
      
      public ExecuteTask(ProcessEventChannel client, String process, String project, String resource) {
         this.client = client;
         this.resource = resource;
         this.process = process;
         this.project = project;
      }
      
      @Override
      public void run() {
         connectSystemStreams();
         // start and listen for the socket close
         long start = System.nanoTime();
         try {
            Executable executable = compiler.compile(resource);
            long middle = System.nanoTime();
            BeginEvent event = new BeginEvent(process, project, resource, TimeUnit.NANOSECONDS.toMillis(middle-start));
            ProfileResultUpdater updater = new ProfileResultUpdater(profiler, client);
            client.send(event);
            
            try {
               updater.start(process); // start sending profile events!!!
               middle = System.nanoTime();
               executable.execute(); // execute the script
            } catch(Exception e) {
               e.printStackTrace();
            }finally {
               try {
                  long stop = System.nanoTime();
                  System.err.flush(); // flush output to sockets
                  System.out.flush();
                  Thread.sleep(200);
                  // should really be a heat map for the editor
                  SortedSet<ProfileResult> lines = profiler.lines(100);
                  System.err.flush();
                  Thread.sleep(2000);
                  System.err.close();
                  System.out.close();
                  ProfileEvent profileEvent = new ProfileEvent(process, lines);
                  ExitEvent exitEvent = new ExitEvent(process, TimeUnit.NANOSECONDS.toMillis(stop-middle));
                  client.send(profileEvent);
                  client.send(exitEvent);
               } catch(Exception e) {
                  e.printStackTrace();
               } finally {
                  System.exit(0); // shutdown when finished  
               }
            }
         } catch (Exception e) {
            System.err.println(ExceptionBuilder.build(e));
         } finally {
            System.exit(0); // shutdown when finished  
         }
      }
      
      private void connectSystemStreams() {
         try {
            ProcessAgentStream error = new ProcessAgentStream(ProcessEventType.WRITE_ERROR, client, process);
            ProcessAgentStream output = new ProcessAgentStream(ProcessEventType.WRITE_OUTPUT, client, process);
            
            // redirect all output to the streams
            System.setOut(new PrintStream(output, false, "UTF-8"));
            System.setErr(new PrintStream(error, false, "UTF-8"));
         }catch(Exception e) {
            System.err.println(ExceptionBuilder.build(e));
         }
      }
   }

   public static void main(String[] list) throws Exception {
      new ProcessAgent(URI.create(list[0]), list[1], Integer.parseInt(list[2])).run();
   }
}
