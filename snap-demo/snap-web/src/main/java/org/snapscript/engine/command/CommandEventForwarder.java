package org.snapscript.engine.command;

import org.snapscript.engine.event.ExitEvent;
import org.snapscript.engine.event.ProcessEventAdapter;
import org.snapscript.engine.event.ProcessEventChannel;
import org.snapscript.engine.event.SyntaxErrorEvent;
import org.snapscript.engine.event.WriteErrorEvent;
import org.snapscript.engine.event.WriteOutputEvent;

public class CommandEventForwarder extends ProcessEventAdapter {
   
   private final CommandClient client;
   
   public CommandEventForwarder(CommandClient client) {
      this.client = client;
   } 
   
   @Override
   public void onWriteError(ProcessEventChannel channel, WriteErrorEvent event) throws Exception {
      try {               
         byte[] array = event.getData();
         int length = event.getLength();
         int offset = event.getOffset();
         String text = new String(array, offset, length, "UTF-8");
         client.sendWriteError(text);
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   @Override
   public void onWriteOutput(ProcessEventChannel channel, WriteOutputEvent event) throws Exception {
      try {               
         byte[] array = event.getData();
         int length = event.getLength();
         int offset = event.getOffset();
         String text = new String(array, offset, length, "UTF-8");
         client.sendWriteOutput(text);
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   @Override
   public void onSyntaxError(ProcessEventChannel channel, SyntaxErrorEvent event) throws Exception {
      try { 
         String resource = event.getResource();
         int line = event.getLine();
         client.sendSyntaxError(resource, line);
      } catch(Exception e) {
         e.printStackTrace();
      }
   }

   public void onExit(ProcessEventChannel channel, ExitEvent event) throws Exception {
      try {               
         client.sendProcessExit();
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public void onClose(ProcessEventChannel channel) throws Exception {
      try {               
         client.sendProcessTerminate();
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   
}