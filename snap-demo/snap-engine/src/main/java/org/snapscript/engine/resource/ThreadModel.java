package org.snapscript.engine.resource;

public enum ThreadModel {
   ASYNCHRONOUS(false),
   SYNCHRONOUS(true);
   
   public boolean synchronous;
   
   private ThreadModel(boolean synchronous) {
      this.synchronous = synchronous;
   }
   
   public boolean isSynchronous() {
      return synchronous;
   }
}
