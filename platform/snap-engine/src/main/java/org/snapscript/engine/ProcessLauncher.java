package org.snapscript.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.agent.ConsoleLogger;
import org.snapscript.agent.event.ProcessEventChannel;
import org.snapscript.engine.http.loader.RemoteProcessBuilder;
import org.snapscript.engine.http.loader.RemoteProcessLauncher;

public class ProcessLauncher {
   
   private final ProcessEventChannel channel;
   private final ConsoleLogger logger;
   private final AtomicLong counter;
   private final File directory;
   
   public ProcessLauncher(ProcessEventChannel channel, ConsoleLogger logger, File directory) {
      this.directory = new File(directory, RemoteProcessBuilder.TEMP_PATH);
      this.counter = new AtomicLong();
      this.channel = channel;
      this.logger = logger;
   }

   public ProcessDefinition launch(ProcessConfiguration configuration) throws Exception {
      int remote = channel.port();
      long sequence = counter.getAndIncrement();
      long time = System.currentTimeMillis();
      int httpPort = configuration.getPort();
      String port = String.valueOf(remote);
      String home = System.getProperty("java.home");
      String name = String.format("agent-%s%s", sequence, time);
      String java = String.format("%s%sbin%s/java", home, File.separatorChar, File.separatorChar);
      String classPath = configuration.getClassPath();
      String resources = String.format("http://localhost:%s/resource/", httpPort);
      String classes = String.format("http://localhost:%s/class/", httpPort);
      Map<String, String> variables = configuration.getVariables();
      List<String> arguments = configuration.getArguments();
      String launcher = RemoteProcessLauncher.class.getCanonicalName();
      String target = ProcessRunner.class.getCanonicalName();
      
      if(classPath == null) {
         classPath = System.getProperty("java.class.path");
      }
      List<String> command = new ArrayList<String>();
      
      command.add(java);
      command.addAll(arguments);
      command.add("-cp");
      command.add(classPath);
      command.add(launcher);
      command.add(classes);
      command.add(target);
      command.add("org.snapscript.");
      command.add(resources);
      command.add(name);
      command.add(port);

      ProcessBuilder builder = new ProcessBuilder(command);
      
      if(!variables.isEmpty()) {
         Map<String, String> environment = builder.environment();
         environment.putAll(variables);
      }
      logger.log(name + ": " +command);
      builder.directory(directory);
      builder.redirectErrorStream(true);
      
      Process process = builder.start();
      return new ProcessDefinition(process, name);
   }
}
