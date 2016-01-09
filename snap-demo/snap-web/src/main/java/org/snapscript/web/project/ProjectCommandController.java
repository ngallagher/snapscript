package org.snapscript.web.project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.simpleframework.http.socket.Frame;
import org.simpleframework.http.socket.FrameChannel;
import org.simpleframework.http.socket.FrameListener;
import org.simpleframework.http.socket.FrameType;
import org.simpleframework.http.socket.Reason;
import org.simpleframework.http.socket.Session;
import org.snapscript.web.ExceptionBuilder;
import org.snapscript.web.WebScriptEngine;
import org.snapscript.web.message.MessageType;

public class ProjectCommandController implements FrameListener {
   
   private static final String SAVE_COMMAND = "save";
   private static final String SUSPEND_COMMAND = "suspend";
   private static final String DELETE_COMMAND = "delete";
   private static final String EXECUTE_COMMAAND = "execute";
   
   private final ProjectScriptValidator validator;
   private final WebScriptEngine engine;
   private final File projectPath;
   private final String project;
   private final String agent;
   
   public ProjectCommandController(WebScriptEngine engine, File projectPath, String project, String agent) {
      this.validator = new ProjectScriptValidator();
      this.engine = engine;
      this.projectPath = projectPath;
      this.project = project;
      this.agent = agent;
   }

   public void onFrame(Session socket, Frame frame) {
      FrameType type = frame.getType();

      if(type == FrameType.TEXT){
         String text = frame.getText();
         
         try {
            if(text.startsWith(EXECUTE_COMMAAND)) {
               String value = text.substring(EXECUTE_COMMAAND.length() + 1);
               executeCommand(socket, value);
            } else if(text.startsWith(SUSPEND_COMMAND)) {
               String value = text.substring(SUSPEND_COMMAND.length() + 1);
               int index = value.indexOf(":");
               String resource = value.substring(0, index);
               String line = value.substring(index + 1);
               suspendCommand(socket, resource, Integer.parseInt(line));
            }else if(text.startsWith(DELETE_COMMAND)) {
               String value = text.substring(DELETE_COMMAND.length() + 1);
               deleteCommand(socket, value);
            }else if(text.startsWith(SAVE_COMMAND)) {
               String value = text.substring(SAVE_COMMAND.length() + 1);
               saveCommand(socket, value);
            }
         } catch(Exception e){
            e.printStackTrace();
         }
      } 
      System.err.println("onFrame(" + type + ")");
   }

   public void onError(Session socket, Exception cause) {
      System.err.println("onError(" + ExceptionBuilder.build(cause) + ")");
   }

   public void onClose(Session session, Reason reason) {
      System.err.println("onClose(" + reason + ")");
   }
   
   private void saveCommand(Session socket, String text){
      
   }
   private void suspendCommand(Session socket, String resource, int line) {
      System.err.println("suspend: " +resource + " at " + line);
      
   }
   private void executeCommand(Session socket, String text) {
      try {
         File scriptDir = new File(projectPath, "temp"); // only one command, send the script to run
         
         if(!scriptDir.exists()) {
            scriptDir.mkdirs();
         }
         String scriptFile = "temp"+System.currentTimeMillis()+".snap";
         String remotePath = "/temp/"+ scriptFile;
         int line = validator.parse(remotePath, text);
         
         if(line == -1) {
            File file = new File(scriptDir, scriptFile);
            FileOutputStream out = new FileOutputStream(file);
            OutputStreamWriter encoder = new OutputStreamWriter(out, "UTF-8");
            encoder.write(text);
            encoder.close();
            engine.executeScript(file, project, remotePath, agent, System.getProperty("os.name"));
         } else {
            FrameChannel channel = socket.getChannel();
            channel.send(MessageType.SYNTAX_ERROR.prefix+""+line+":Syntax error at line " + line);
         }
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   private void deleteCommand(Session socket, String text) {
      
   }

}
