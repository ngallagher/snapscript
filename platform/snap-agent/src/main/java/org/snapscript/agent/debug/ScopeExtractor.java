package org.snapscript.agent.debug;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.snapscript.core.Scope;

public class ScopeExtractor implements ScopeBrowser {

   private final ScopeNodeTraverser traverser;
   private final Set<String> paths;
   
   public ScopeExtractor(Scope scope) {
      this.traverser = new ScopeNodeTraverser(scope);
      this.paths = new CopyOnWriteArraySet<String>();
   }
   
   public Map<String, Map<String, String>> build() {
      return traverser.expand(paths);
   }
   
   @Override
   public void browse(Set<String> expand) {
      paths.clear();
      paths.addAll(expand);
   }
  
}
