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

   public void launch(String root) throws Exception {
      String home = System.getProperty("java.home");
      String path = System.getProperty("java.class.path");
      String type = ProcessAgent.class.getCanonicalName();
      long sequence = counter.getAndIncrement();
      int port = channel.port();
      ProcessBuilder builder = new ProcessBuilder(
            String.format("%s%sbin%s/java", home, File.separatorChar, File.separatorChar), 
            "-XX:+UnlockCommercialFeatures",
            "-XX:+FlightRecorder",
            "-cp", 
            path, 
            type, 
            root,
            String.format("agent-%s", sequence),
            String.valueOf(port));
      builder.redirectErrorStream(true);
      builder.start();
   }
}
