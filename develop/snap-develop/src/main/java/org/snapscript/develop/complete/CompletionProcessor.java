package org.snapscript.develop.complete;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.snapscript.agent.ConsoleLogger;
import org.snapscript.develop.ConfigurationClassLoader;
import org.snapscript.develop.http.project.Project;

public class CompletionProcessor {
   
   private final CompletionMatcherBuilder builder;
   private final CompletionThreadPool pool;
   
   public CompletionProcessor(ConfigurationClassLoader loader, ConsoleLogger logger) {
      this.builder = new CompletionMatcherBuilder(logger);
      this.pool = new CompletionThreadPool(loader, 6);
   }

   public Map<String, String> createTokens(CompletionRequest request, Project project) {
      CompletionMatcher finder = builder.compile();
      CompletionState state = createState(request, project);
      
      return finder.findTokens(state);
   }
   
   private CompletionState createState(CompletionRequest request, Project project){
      String prefix = request.getPrefix();
      String source = request.getSource();
      String resource = request.getResource();
      String complete = request.getComplete();
      File root = project.getProjectPath();
      String lines[] = source.split("\\r?\\n");
      List<String> list = Arrays.asList(lines);
      int line = request.getLine();
      
      return new CompletionState(pool, list, root, source, resource, prefix, complete, line);
   }
}
