package org.snapscript.engine;

import org.snapscript.engine.http.WebServer;

public class ProcessEngineServer {

   private final ProcessEngine engine;
   private final WebServer server;
   
   public ProcessEngineServer(ProcessEngine engine, WebServer server) {
      this.engine = engine;
      this.server = server;
   }
   
   public void start() {
      try {
         int port = server.start();
         String resource = String.format("http://localhost:%s/resource", port);
         String project = String.format("http://localhost:%s/project/default", port);
         String script = ProcessEngineArgument.SCRIPT.getValue();
            
         if(script != null) {
            engine.launch(); // start a new process
         }
         System.err.println(project);
         engine.start(resource);
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
}
