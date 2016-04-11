package org.snapscript.develop.complete;

import java.io.PrintStream;
import java.util.Map;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.agent.ConsoleLogger;
import org.snapscript.develop.http.project.Project;
import org.snapscript.develop.http.project.ProjectBuilder;
import org.snapscript.develop.http.resource.Resource;

import com.google.gson.Gson;

// /complete/<project>
public class CompletionResource implements Resource {

   private final ProjectBuilder builder;
   private final CompletionProcessor completer;
   private final Gson gson;
   
   public CompletionResource(ProjectBuilder builder, ConsoleLogger logger) {
      this.completer = new CompletionProcessor(logger);
      this.gson = new Gson();
      this.builder = builder;
   }

   @Override
   public void handle(Request request, Response response) throws Throwable {
      CompletionResponse result = new CompletionResponse();
      PrintStream out = response.getPrintStream();
      String content = request.getContent();
      Path path = request.getPath();
      Project project = builder.createProject(path);
      CompletionRequest context = gson.fromJson(content, CompletionRequest.class);
      Map<String, String> tokens = completer.createTokens(context, project);
      result.setTokens(tokens);
      String text = gson.toJson(result);
      response.setContentType("application/json");
      out.println(text);
      out.close();
   }
}
