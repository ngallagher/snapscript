package org.snapscript.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.web.resource.Resource;

// should execute the script and stream the result
public class ScriptResource implements Resource {

   private final ScriptEngine engine;
   
   public ScriptResource(ScriptEngine engine) {
      this.engine = engine;
   }

   @Override
   public void handle(Request request, Response response) throws Exception {
      PrintStream stream = response.getPrintStream();
      String source = request.getParameter("script");
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
      engine.executeScript(output, info, file, System.getProperty("os.name"));
   }
}
