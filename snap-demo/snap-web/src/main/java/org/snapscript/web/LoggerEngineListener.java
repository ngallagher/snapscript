package org.snapscript.web;

public class LoggerEngineListener implements ScriptEngineListener {

   @Override
   public void onJoin(String os) {
      System.err.println("JOIN: "+os);
   }

}
