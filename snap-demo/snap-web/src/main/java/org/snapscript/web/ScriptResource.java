package org.snapscript.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.web.resource.Resource;

// NEEDS TO BE A WEBSOCKET SERVICE!!!!
public class ScriptResource implements Resource {

   private final WebScriptEngine engine;
   
   public ScriptResource(WebScriptEngine engine) {
      this.engine = engine;
   }

   @Override
   public void handle(Request request, Response response) throws Exception {
      PrintStream stream = response.getPrintStream();
      String source = request.getParameter("script");
      String processId = request.getParameter("processId");
      ConsoleWriter output = new ConsoleWriter(stream, "red");
      ConsoleWriter info = new ConsoleWriter(stream, "black");
      File tempPath = new File("temp");
      if(!tempPath.exists()) {
         tempPath.mkdirs();
      }
      File file = new File(tempPath, "temp"+System.currentTimeMillis()+".snap");
      FileOutputStream out = new FileOutputStream(file);
      OutputStreamWriter encoder = new OutputStreamWriter(out, "UTF-8");
      encoder.write(source);
      encoder.close();
      response.setContentType("text/plain");
      engine.executeScript(file, processId, System.getProperty("os.name"));
   }
}
