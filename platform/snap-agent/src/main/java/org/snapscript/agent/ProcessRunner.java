package org.snapscript.agent;

import java.net.URI;

public class ProcessRunner {

   public static void main(String[] list) throws Exception {
      URI root = URI.create(list[0]);
      String process = list[1];
      int port = Integer.parseInt(list[2]);
      
      start(root, process, port);
   }
   
   public static void start(URI root, String process, int port) throws Exception {
      ProcessAgent runner = new ProcessAgent(root, process, port);
      runner.run();
   }
}
