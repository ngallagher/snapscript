package org.snapscript.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.agent.event.ProcessEventChannel;

public class ProcessLauncher {
   
   private final ProcessEventChannel channel;
   private final AtomicLong counter;
   
   public ProcessLauncher(ProcessEventChannel channel) {
      this.counter = new AtomicLong();
      this.channel = channel;
   }

   public void launch(ProcessConfiguration configuration) throws Exception {
      int remote = channel.port();
      long sequence = counter.getAndIncrement();
      long time = System.currentTimeMillis();
      String port = String.valueOf(remote);
      String home = System.getProperty("java.home");
      String name = String.format("agent-%s%s", sequence, time);
      String java = String.format("%s%sbin%s/java", home, File.separatorChar, File.separatorChar);
      String classPath = configuration.getClassPath();
      String address = configuration.getAddress();
      Map<String, String> variables = configuration.getVariables();
      List<String> arguments = configuration.getArguments();
      String type = ProcessRunner.class.getCanonicalName();
      
      if(classPath == null) {
         classPath = System.getProperty("java.class.path");
      }
      List<String> command = new ArrayList<String>();
      
      command.add(java);
      command.addAll(arguments);
      command.add("-cp");
      command.add(classPath);
      command.add(type);
      command.add(address);
      command.add(name);
      command.add(port);

      ProcessBuilder builder = new ProcessBuilder(command);
      
      if(!variables.isEmpty()) {
         Map<String, String> environment = builder.environment();
         environment.putAll(variables);
      }
      builder.redirectErrorStream(true);
      builder.start();
   }
}
