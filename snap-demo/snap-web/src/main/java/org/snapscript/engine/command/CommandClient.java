package org.snapscript.engine.command;

import java.util.HashMap;
import java.util.Map;

import org.simpleframework.http.socket.FrameChannel;

import com.google.gson.Gson;

public class CommandClient {
   
   public static final String PRINT_ERROR = "PRINT_ERROR";
   public static final String PRINT_OUTPUT = "PRINT_OUTPUT";
   public static final String SYNTAX_ERROR = "SYNTAX_ERROR";
   public static final String RELOAD_TREE = "RELOAD_TREE";
   public static final String PROCESS_TERMINATE = "TERMINATE";
   public static final String PROCESS_EXIT = "EXIT";
   
   private final FrameChannel channel;
   private final String project;
   private final Gson gson;
   
   public CommandClient(FrameChannel channel, String project) {
      this.gson = new Gson();
      this.channel = channel;
      this.project = project;
   } 
   
   public void sendSyntaxError(String resource, int line) throws Exception {
      Map<String, Object> properties = new HashMap<String, Object>();
      properties.put("line", line);
      properties.put("resource", resource);
      properties.put("description", "Syntax error at line " + line);
      properties.put("project", project);
      String json = gson.toJson(properties);
      channel.send(SYNTAX_ERROR + ":"+json);
   }
   
   public void sendWriteError(String text) throws Exception {
      channel.send(PRINT_ERROR + ":"+text);
   }
   
   public void sendWriteOutput(String text) throws Exception {
      channel.send(PRINT_ERROR + ":"+text);
   }
   
   public void sendProcessTerminate() throws Exception {
      channel.send(PROCESS_TERMINATE);
   }
   
   public void sendProcessExit() throws Exception {
      channel.send(PROCESS_EXIT);
   }
   
   public void sendReloadTree() throws Exception {
      channel.send(RELOAD_TREE);
   }
}
