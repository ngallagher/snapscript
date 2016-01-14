package org.snapscript.web.agent;

import org.simpleframework.http.socket.FrameChannel;
import org.snapscript.web.binary.event.ExitEvent;
import org.snapscript.web.binary.event.ProcessEventAdapter;
import org.snapscript.web.binary.event.ProcessEventChannel;
import org.snapscript.web.binary.event.SyntaxErrorEvent;
import org.snapscript.web.binary.event.WriteErrorEvent;
import org.snapscript.web.binary.event.WriteOutputEvent;

public class ProcessEventRouter extends ProcessEventAdapter {
   
   public static final String PRINT_ERROR = "PRINT_ERROR";
   public static final String PRINT_OUTPUT = "PRINT_OUTPUT";
   public static final String SYNTAX_ERROR = "SYNTAX_ERROR";
   public static final String PROCESS_TERMINATE = "TERMINATE";
   public static final String PROCESS_EXIT = "EXIT";
   
   private final FrameChannel socket;
   
   public ProcessEventRouter(FrameChannel socket) {
      this.socket = socket;
   } 
   
   @Override
   public void onWriteError(ProcessEventChannel channel, WriteErrorEvent event) throws Exception {
      try {               
         byte[] array = event.getData();
         int length = event.getLength();
         int offset = event.getOffset();
         String text = new String(array, offset, length, "UTF-8");
         socket.send(PRINT_ERROR + ":" + text);
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
         socket.send(PRINT_OUTPUT + ":" + text);
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   @Override
   public void onSyntaxError(ProcessEventChannel channel, SyntaxErrorEvent event) throws Exception {
      try { 
         String resource = event.getResource();
         int line = event.getLine();
         socket.send(SYNTAX_ERROR + ":" + resource + ":" + line);
      } catch(Exception e) {
         e.printStackTrace();
      }
   }

   public void onExit(ProcessEventChannel channel, ExitEvent event) throws Exception {
      try {               
         socket.send(PROCESS_EXIT);
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public void onClose(ProcessEventChannel channel) throws Exception {
      try {               
         socket.send(PROCESS_TERMINATE);
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   
}
