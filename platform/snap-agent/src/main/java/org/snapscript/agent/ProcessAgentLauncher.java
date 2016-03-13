package org.snapscript.agent;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.agent.event.ProcessEventChannel;

public class ProcessAgentLauncher {
   
   private final ProcessEventChannel channel;
   private final AtomicLong counter;
   
   public ProcessAgentLauncher(ProcessEventChannel channel) {
      this.counter = new AtomicLong();
      this.channel = channel;
   }

   public void launch(ProcessAgentConfiguration configuration) throws Exception {
      String home = System.getProperty("java.home");
      String classPath = configuration.getClassPath();
      String address = configuration.getAddress();
      int maxMemoryMegabytes = configuration.getMaxMemory();
      int minMemoryMegabytes = configuration.getMinMemory();
      String maxMemory = "-Xmx200m";
      String minMemory = "-Xms10m";
      
      if(classPath == null) {
         classPath = System.getProperty("java.class.path");
      }
      if(maxMemoryMegabytes > 0) {
         maxMemory = "-Xmx" + maxMemoryMegabytes + "m";
      }
      if(minMemoryMegabytes > 0) {
         minMemory = "-Xms" + minMemoryMegabytes + "m";
      }
      String type = ProcessAgent.class.getCanonicalName();
      long sequence = counter.getAndIncrement();
      long time = System.currentTimeMillis();
      int port = channel.port();
      ProcessBuilder builder = new ProcessBuilder(
            String.format("%s%sbin%s/java", home, File.separatorChar, File.separatorChar), 
            "-XX:+UnlockCommercialFeatures",
            "-XX:+FlightRecorder",
            maxMemory,
            minMemory,
            "-cp", 
            classPath, 
            type, 
            address,
            String.format("agent-%s%s", sequence, time),
            String.valueOf(port));
      builder.redirectErrorStream(true);
      builder.start();
   }
}
