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
         String address = server.start();
         engine.start(address);
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
}
