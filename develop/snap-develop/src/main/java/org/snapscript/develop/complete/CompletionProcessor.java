package org.snapscript.develop.complete;

import java.io.File;
import java.util.Map;

import org.snapscript.agent.ConsoleLogger;
import org.snapscript.develop.http.project.Project;

public class CompletionProcessor {
   
   private final CompletionMatcherBuilder builder;
   
   public CompletionProcessor(ConsoleLogger logger) {
      this.builder = new CompletionMatcherBuilder(logger);
   }

   public Map<String, String> complete(Project project, String text, String resource, String prefix, String complete, int line) {
      CompletionMatcher finder = builder.compile();
      File root = project.getProjectPath();
      
      return finder.findTokens(root, text, resource, prefix, complete, line);
   }
}
