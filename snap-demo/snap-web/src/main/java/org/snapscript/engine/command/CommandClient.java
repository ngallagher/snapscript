package org.snapscript.engine.command;

import java.util.Map;

import org.simpleframework.http.socket.FrameChannel;

public class CommandClient {
   
   private final CommandWriter writer;
   private final FrameChannel channel;
   private final String project;
   
   public CommandClient(FrameChannel channel, String project) {
      this.writer = new CommandWriter();
      this.channel = channel;
      this.project = project;
   } 
   
   public void sendScope(String thread, String instruction, String resource, int line, Map<String, String> variables) throws Exception {
      ScopeCommand command = new ScopeCommand(thread, instruction, resource, line, variables);
      String message = writer.write(command);
      channel.send(message);
   }
   
   public void sendStart(String process, String resource) throws Exception {
      StartCommand command = new StartCommand(process, resource);
      String message = writer.write(command);
      channel.send(message);
   }
   
   public void sendSyntaxError(String resource, int line) throws Exception {
      ProblemCommand command = new ProblemCommand(project, "Syntax error at line " + line, resource, line);
      String message = writer.write(command);
      channel.send(message);
   }
   
   public void sendPrintError(String text) throws Exception {
      PrintErrorCommand command = new PrintErrorCommand(text);
      String message = writer.write(command);
      channel.send(message);
   }
   
   public void sendPrintOutput(String text) throws Exception {
      PrintOutputCommand command = new PrintOutputCommand(text);
      String message = writer.write(command);
      channel.send(message);
   }
   
   public void sendProcessExit() throws Exception {
      ExitCommand command = new ExitCommand();
      String message = writer.write(command);
      channel.send(message);
   }
   
   public void sendProcessTerminate() throws Exception {
      TerminateCommand command = new TerminateCommand();
      String message = writer.write(command);
      channel.send(message);
   }
   
   public void sendReloadTree() throws Exception {
      ReloadTreeCommand command = new ReloadTreeCommand();
      String message = writer.write(command);
      channel.send(message);
   }
}
