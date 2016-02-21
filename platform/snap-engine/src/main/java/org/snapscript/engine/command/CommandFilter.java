package org.snapscript.engine.command;

import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.agent.event.ProcessEvent;

public class CommandFilter {

   private final AtomicReference<String> attachment;
   
   public CommandFilter() {
      this.attachment = new AtomicReference<String>();
   }
   
   public String get(){
      return attachment.get();
   }
   
   public void attach(String process) {
      attachment.set(process);
   }
   
   public boolean accept(ProcessEvent event) {
      String process = event.getProcess();
      String focus = attachment.get();
      
      if(focus != null) {
         return process.equals(focus);
      }
      return false;
   }
   
   public void clear() {
      attachment.set(null);
   }
}
