package org.snapscript.engine.http.project;

import java.io.File;
import java.net.Socket;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.socket.FrameChannel;
import org.simpleframework.http.socket.Session;
import org.simpleframework.http.socket.service.Service;
import org.simpleframework.transport.Channel;
import org.snapscript.engine.ProcessEngine;
import org.snapscript.engine.ProcessEngineScript;
import org.snapscript.engine.command.CommandController;
import org.snapscript.engine.command.CommandListener;

public class ProjectScriptService implements Service {
   
   private final ProcessEngineScript script;
   private final ProjectBuilder builder;
   private final ProcessEngine engine;
   
   public ProjectScriptService(ProcessEngine engine, ProcessEngineScript script, ProjectBuilder builder) {
      this.script = script;
      this.builder = builder;
      this.engine = engine;
   }  
  
   public void connect(Session connection) {
      try {
         FrameChannel frameChannel = connection.getChannel();
         Request request = connection.getRequest();    
         Path path = request.getPath(); // /connect/<project-name>
         Project project = builder.createProject(path);
         File projectPath = project.getProjectPath();
         String projectName = project.getProjectName();
         Channel channel = request.getChannel();
         Socket socket = channel.getSocket().socket();
         int port = socket.getLocalPort();
         String name = String.valueOf(port);
         
         try {
            CommandListener listener = new CommandListener(engine, frameChannel, projectPath, projectName, name);
            CommandController controller = new CommandController(listener);
            
            frameChannel.register(controller);
            script.connect(listener, path); // if there is a script then execute it
         } catch(Exception e) {
            e.printStackTrace();
         }
      }catch(Exception e){
         e.printStackTrace();
      }
      
   }
}
