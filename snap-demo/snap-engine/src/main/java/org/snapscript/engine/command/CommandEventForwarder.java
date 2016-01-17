package org.snapscript.engine.command;

import java.util.Map;

import org.snapscript.agent.event.ExitEvent;
import org.snapscript.agent.event.ProcessEventAdapter;
import org.snapscript.agent.event.ProcessEventChannel;
import org.snapscript.agent.event.ScopeEvent;
import org.snapscript.agent.event.StartEvent;
import org.snapscript.agent.event.SyntaxErrorEvent;
import org.snapscript.agent.event.WriteErrorEvent;
import org.snapscript.agent.event.WriteOutputEvent;

public class CommandEventForwarder extends ProcessEventAdapter {
   
   private final CommandClient client;
   
   public CommandEventForwarder(CommandClient client) {
      this.client = client;
   } 
   
   @Override
   public void onScope(ProcessEventChannel channel, ScopeEvent event) throws Exception {
      Map<String, Map<String, String>> variables = event.getVariables();
      String thread = event.getThread();
      String instruction = event.getInstruction();
      String status = event.getStatus();
      String resource = event.getResource();
      int depth = event.getDepth();
      int line = event.getLine();
      client.sendScope(thread, instruction, status, resource, line, depth, variables);
   }
   
   @Override
   public void onWriteError(ProcessEventChannel channel, WriteErrorEvent event) throws Exception {             
      byte[] array = event.getData();
      int length = event.getLength();
      int offset = event.getOffset();
      String text = new String(array, offset, length, "UTF-8");
      client.sendPrintError(text);
   }
   
   @Override
   public void onWriteOutput(ProcessEventChannel channel, WriteOutputEvent event) throws Exception {            
      byte[] array = event.getData();
      int length = event.getLength();
      int offset = event.getOffset();
      String text = new String(array, offset, length, "UTF-8");
      client.sendPrintOutput(text);
   }
   
   @Override
   public void onSyntaxError(ProcessEventChannel channel, SyntaxErrorEvent event) throws Exception {
      String resource = event.getResource();
      int line = event.getLine();
      client.sendSyntaxError(resource, line);
   }
   
   @Override
   public void onStart(ProcessEventChannel channel, StartEvent event) throws Exception {
      String resource = event.getResource();
      String process = event.getProcess();
      client.sendStart(process, resource);
   }
   
   @Override
   public void onExit(ProcessEventChannel channel, ExitEvent event) throws Exception {              
      client.sendProcessExit();
   }
   
   @Override
   public void onClose(ProcessEventChannel channel) throws Exception {             
      client.sendProcessTerminate();
   }
}
