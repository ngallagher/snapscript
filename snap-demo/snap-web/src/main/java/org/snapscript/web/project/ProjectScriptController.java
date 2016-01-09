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

public class ProjectScriptController implements FrameListener {

   private final ProjectScriptValidator validator;
   private final WebScriptEngine engine;
   private final File projectPath;
   private final String project;
   private final String agent;
   
   public ProjectScriptController(WebScriptEngine engine, File projectPath, String project, String agent) {
      this.validator = new ProjectScriptValidator();
      this.engine = engine;
      this.projectPath = projectPath;
      this.project = project;
      this.agent = agent;
   }

   public void onFrame(Session socket, Frame frame) {
      FrameType type = frame.getType();
      String text = frame.getText();
      
      if(type == FrameType.TEXT){
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

}
