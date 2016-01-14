package org.snapscript.web.agent;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.web.agent.ProcessAgent;

public class ProcessAgentLauncher {
   
   private final AtomicLong counter;
   private final String root;
   private final int port;
   
   public ProcessAgentLauncher(String root, int port) {
      this.counter = new AtomicLong();
      this.root = root;
      this.port = port;
   }

   public void launch() throws IOException {
      String home = System.getProperty("java.home");
      String path = System.getProperty("java.class.path");
      String type = ProcessAgent.class.getCanonicalName();
      long sequence = counter.getAndIncrement();
      ProcessBuilder builder = new ProcessBuilder(
            String.format("%s%sbin%s/java", home, File.separatorChar, File.separatorChar), 
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
