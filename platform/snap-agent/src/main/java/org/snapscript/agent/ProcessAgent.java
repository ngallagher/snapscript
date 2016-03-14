package org.snapscript.agent;

import java.net.URI;

import org.snapscript.agent.debug.BreakpointMatcher;
import org.snapscript.agent.debug.SuspendController;
import org.snapscript.agent.debug.SuspendInterceptor;
import org.snapscript.agent.event.ProcessEventChannel;
import org.snapscript.agent.event.RegisterEvent;
import org.snapscript.agent.event.socket.SocketEventClient;
import org.snapscript.agent.profiler.ProcessProfiler;
import org.snapscript.core.TraceInterceptor;

public class ProcessAgent {

   private final ProcessContext context;
   private final String process;
   private final URI root;
   private final int port;

   public ProcessAgent(URI root, String process, int port) {
      this.context = new ProcessContext(root, process, port);
      this.process = process;
      this.root = root;
      this.port = port;
   }
   
   public void run() throws Exception {
      BreakpointMatcher matcher = context.getMatcher();
      SuspendController controller = context.getController();
      TraceInterceptor interceptor = context.getInterceptor();
      ProcessProfiler profiler = context.getProfiler();
      String system = System.getProperty("os.name");
      String host = root.getHost();
      
      try {
         ConnectionChecker checker = new ConnectionChecker(process, system);
         RegisterEvent register = new RegisterEvent(process, system);
         ProcessEventReceiver listener = new ProcessEventReceiver(context, checker);
         SocketEventClient client = new SocketEventClient(listener);
         ProcessEventChannel channel = client.connect(host, port);
         SuspendInterceptor suspender = new SuspendInterceptor(channel, matcher, controller, process);
         
         interceptor.register(profiler);
         interceptor.register(suspender);
         channel.send(register); // send the initial register event
         checker.start();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
