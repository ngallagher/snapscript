package org.snapscript.engine.agent.debug;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SuspendLatch {
   
   private final Map<String, ResumeListener> listeners;
   private final Map<String, ResumeType> types;
   private final Map<String, Object> locks;
   
   public SuspendLatch() {
      this.listeners = new ConcurrentHashMap<String, ResumeListener>();
      this.types = new ConcurrentHashMap<String, ResumeType>();
      this.locks = new ConcurrentHashMap<String, Object>();
   }

   public ResumeType suspend(ResumeListener listener) {
      String name = Thread.currentThread().getName();
      Object lock = locks.get(name);
      
      if(lock == null) {
         lock = new Object();
         locks.put(name, lock);
      }
      synchronized(lock) {
         try {
            listeners.put(name, listener);
            lock.wait();
         }catch(Exception e) {
            throw new IllegalStateException("Could not suspend thread '" + name + "'", e);
         }
      }
      return types.remove(name); // resume in a specific way
   }
   
   public void resume(ResumeType type, String thread) {
      Object lock = locks.get(thread);
      ResumeListener listener = listeners.remove(thread);
      
      synchronized(lock) {
         try {
            if(listener != null) {
               types.put(thread, type);
               listener.resume(thread);
            }
            lock.notify();
         }catch(Exception e) {
            throw new IllegalStateException("Could not resume thread '" + thread + "'", e);
         }
      }
   }
}
