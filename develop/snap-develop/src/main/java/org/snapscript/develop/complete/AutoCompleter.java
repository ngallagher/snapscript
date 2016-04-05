package org.snapscript.develop.complete;

import java.util.Set;

import org.snapscript.develop.common.TokenFinder;
import org.snapscript.develop.common.TokenFinderBuilder;
import org.snapscript.develop.http.project.Project;

public class AutoCompleter {
   
   private final TokenFinderBuilder builder;
   
   public AutoCompleter() {
      this.builder = new TokenFinderBuilder();
   }

   public Set<String> complete(Project project, String text, String resource, String prefix) {
      TokenFinder finder = builder.compile();
      return finder.findTokens(text, resource, prefix);
   }
}
