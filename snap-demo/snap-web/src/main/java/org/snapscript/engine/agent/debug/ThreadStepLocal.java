package org.snapscript.engine.agent.debug;

public class ThreadStepLocal extends ThreadLocal<ThreadStep> {

   @Override
   protected ThreadStep initialValue() {
      return new ThreadStep();
   }
}
