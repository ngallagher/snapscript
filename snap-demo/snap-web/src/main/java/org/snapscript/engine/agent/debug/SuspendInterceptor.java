package org.snapscript.engine.agent.debug;

import org.snapscript.core.Scope;
import org.snapscript.core.TraceInterceptor;

public class SuspendInterceptor implements TraceInterceptor {
   
   private final SuspendMatcher matcher;
   
   public SuspendInterceptor(SuspendMatcher matcher) {
      this.matcher = matcher;
   }

   @Override
   public void before(Scope scope, Object instruction, String resource, int line, int key) {
      if(matcher.match(resource, line)) {
         System.err.println("resource=["+resource+"] line=["+line+"]");
      }
   }

   @Override
   public void after(Scope scope, Object instruction, String resource, int line, int key) {
      matcher.match(resource, line);
   }

}
