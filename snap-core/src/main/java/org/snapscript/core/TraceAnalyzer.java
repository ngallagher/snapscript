package org.snapscript.core;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class TraceAnalyzer implements TraceInterceptor {
   
   private final Set<TraceInterceptor> interceptors;
   
   public TraceAnalyzer() {
      this.interceptors = new CopyOnWriteArraySet<TraceInterceptor>();
   }
   
   @Override
   public void before(Scope scope, Object instruction, int line) {
      for(TraceInterceptor interceptor : interceptors) {
         interceptor.before(scope, instruction, line);
      }
   }
   
   @Override
   public void after(Scope scope, Object instruction, int line) {
      for(TraceInterceptor interceptor : interceptors) {
         interceptor.after(scope, instruction, line   );
      }
   }

   public void register(TraceInterceptor interceptor) {
      interceptors.add(interceptor);
   }
   
   public void remove(TraceInterceptor interceptor) {
      interceptors.remove(interceptor);
   }
}