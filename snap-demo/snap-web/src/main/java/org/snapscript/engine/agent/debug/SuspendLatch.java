package org.snapscript.engine.agent.debug;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SuspendLatch {
   
   private final Map<String, ResumeListener> listeners;
   private final Map<String, Object> locks;
   
   public SuspendLatch() {
      this.listeners = new ConcurrentHashMap<String, ResumeListener>();
      this.locks = new ConcurrentHashMap<String, Object>();
   }

   public void suspend(ResumeListener listener) {
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
   }
   
   public void resume(String thread) {
      Object lock = locks.get(thread);
      ResumeListener listener = listeners.remove(thread);
      
      synchronized(lock) {
         try {
            if(listener != null) {
               listener.resume(thread);
            }
            lock.notify();
         }catch(Exception e) {
            throw new IllegalStateException("Could not resume thread '" + thread + "'", e);
         }
      }
   }
}
