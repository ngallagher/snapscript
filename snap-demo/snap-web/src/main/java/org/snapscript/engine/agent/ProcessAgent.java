package org.snapscript.engine.agent;

import java.io.PrintStream;
import java.net.URI;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

import org.snapscript.compile.Executable;
import org.snapscript.compile.ResourceCompiler;
import org.snapscript.core.Module;
import org.snapscript.core.Package;
import org.snapscript.core.PackageLinker;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.resource.RemoteReader;
import org.snapscript.engine.ExceptionBuilder;
import org.snapscript.engine.ScriptAgentContext;
import org.snapscript.engine.ScriptProfiler;
import org.snapscript.engine.ScriptProfiler.ProfileResult;
import org.snapscript.engine.ScriptResourceReader;
import org.snapscript.engine.agent.debug.BreakpointMatcher;
import org.snapscript.engine.agent.debug.ResumeType;
import org.snapscript.engine.agent.debug.SuspendInterceptor;
import org.snapscript.engine.agent.debug.SuspendLatch;
import org.snapscript.engine.event.BreakpointsEvent;
import org.snapscript.engine.event.ExecuteEvent;
import org.snapscript.engine.event.ExitEvent;
import org.snapscript.engine.event.PingEvent;
import org.snapscript.engine.event.PongEvent;
import org.snapscript.engine.event.ProcessEventAdapter;
import org.snapscript.engine.event.ProcessEventChannel;
import org.snapscript.engine.event.ProcessEventType;
import org.snapscript.engine.event.RegisterEvent;
import org.snapscript.engine.event.StartEvent;
import org.snapscript.engine.event.StepEvent;
import org.snapscript.engine.event.socket.SocketEventClient;

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

   private final ScriptResourceReader reader;
   private final ScriptAgentContext context;
   private final ResourceCompiler compiler;
   private final ScriptProfiler profiler;
   private final BreakpointMatcher matcher;
   private final RemoteReader remoteReader;
   private final SuspendLatch latch;
   private final String process;
   private final int port;

   public ProcessAgent(URI rootURI, String process, int port) {
      this.remoteReader = new RemoteReader(rootURI);
      this.reader = new ScriptResourceReader(remoteReader);
      this.context = new ScriptAgentContext(reader);
      this.compiler = new ResourceCompiler(context);
      this.matcher = new BreakpointMatcher();
      this.profiler = new ScriptProfiler();
      this.latch = new SuspendLatch();
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
         Scope scope = module.getScope();
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
         SuspendInterceptor interceptor = new SuspendInterceptor(channel, matcher, latch, process);
         
         analyzer.register(profiler);
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
         reader.update(project); // XXX rubbish
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
            latch.resume(ResumeType.RUN, thread);
         } else if(type == StepEvent.STEP_IN) {
            latch.resume(ResumeType.STEP_IN, thread);
         } else if(type == StepEvent.STEP_OUT) {
            latch.resume(ResumeType.STEP_OUT, thread);
         } else if(type == StepEvent.STEP_OVER) {
            latch.resume(ResumeType.STEP_OVER, thread);
         }
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
         try {
            StartEvent event = new StartEvent(process, project, resource);
            client.send(event);
            execute(); // execute the script
         } catch(Exception e) {
            e.printStackTrace();
         }finally {
            try {
               System.err.flush(); // flush output to sockets
               System.out.flush();
               Thread.sleep(200);
               // should really be a heat map for the editor
               SortedSet<ProfileResult> lines = profiler.lines();
               System.err.println();
               for(ProfileResult entry : lines) {
                  int line = entry.getLine();
                  long time = entry.getTime();
                  System.err.println("Line " + line + " took " + time + " ms");
               }
               System.err.flush();
               Thread.sleep(2000);
               System.err.close();
               System.out.close();
               ExitEvent event = new ExitEvent(process);
               client.send(event);
            } catch(Exception e) {
               e.printStackTrace();
            } finally {
               System.exit(0); // shutdown when finished  
            }
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

      private void execute() {
         try {
            connectSystemStreams();
            
            // start and listen for the socket close
            long start = System.nanoTime();
            Executable executable = compiler.compile(resource);
            long middle = System.nanoTime();
            System.err.println("Compile time " + TimeUnit.NANOSECONDS.toMillis(middle-start));
            executable.execute();
            long stop = System.nanoTime();
            System.out.flush();
            System.err.flush();
            System.err.println();
            
            //client.getPublisher().publish(MessageType.COMPILE_TIME, TimeUnit.NANOSECONDS.toMillis(middle-start));
            //client.getPublisher().publish(MessageType.EXECUTE_TIME, TimeUnit.NANOSECONDS.toMillis(stop-middle));
            System.err.println("Execute time " + TimeUnit.NANOSECONDS.toMillis(stop-middle));
         } catch (Exception e) {
            System.err.println(ExceptionBuilder.build(e));
         }
      }
   }

   public static void main(String[] list) throws Exception {
      new ProcessAgent(URI.create(list[0]), list[1], Integer.parseInt(list[2])).run();
   }
}
