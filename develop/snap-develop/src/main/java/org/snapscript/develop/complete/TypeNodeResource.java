package org.snapscript.develop.complete;

import java.io.PrintStream;
import java.util.Map;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.agent.ConsoleLogger;
import org.snapscript.develop.configuration.ConfigurationClassLoader;
import org.snapscript.develop.http.project.ProjectBuilder;
import org.snapscript.develop.http.resource.Resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// /type/<project>
public class TypeNodeResource implements Resource {

   private final ConfigurationClassLoader loader;
   private final TypeNodeScanner scanner;
   private final Gson gson;
   
   public TypeNodeResource(ProjectBuilder builder, ConfigurationClassLoader loader, ConsoleLogger logger) {
      this.scanner = new TypeNodeScanner(builder, loader, logger);
      this.gson = new GsonBuilder().setPrettyPrinting().create();
      this.loader = loader;
   }

   @Override
   public void handle(Request request, Response response) throws Throwable {
      String prefix = request.getParameter("prefix");
      PrintStream out = response.getPrintStream();
      Path path = request.getPath();
      Thread thread = Thread.currentThread();
      ClassLoader classLoader = loader.getClassLoader();
      thread.setContextClassLoader(classLoader);
      Map<String, String> tokens = scanner.compileProject(path, prefix);
      String text = gson.toJson(tokens);
      response.setContentType("application/json");
      out.println(text);
      out.close();
   }
}
