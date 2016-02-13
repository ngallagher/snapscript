package org.snapscript.core;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class TraceAnalyzer implements TraceListener {
   
   private final Set<TraceListener> listeners;
   
   public TraceAnalyzer() {
      this.listeners = new CopyOnWriteArraySet<TraceListener>();
   }
   
   @Override
   public void before(Scope scope, Trace trace) {
      if(!listeners.isEmpty()) {
         for(TraceListener listener : listeners) {
            listener.before(scope, trace);
         }
      }
   }
   
   @Override
   public void after(Scope scope, Trace trace) {
      if(!listeners.isEmpty()) {
         for(TraceListener listener : listeners) {
            listener.after(scope, trace);
         }
      }
   }

   public void register(TraceListener listener) {
      listeners.add(listener);
   }
   
   public void remove(TraceListener listener) {
      listeners.remove(listener);
   }
}
