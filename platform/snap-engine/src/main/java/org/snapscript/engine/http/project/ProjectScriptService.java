package org.snapscript.engine.http.project;

import java.io.File;
import java.net.Socket;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.socket.FrameChannel;
import org.simpleframework.http.socket.Session;
import org.simpleframework.http.socket.service.Service;
import org.simpleframework.transport.Channel;
import org.snapscript.agent.ConsoleLogger;
import org.snapscript.engine.ConnectListener;
import org.snapscript.engine.ProcessManager;
import org.snapscript.engine.command.CommandController;
import org.snapscript.engine.command.CommandListener;

public class ProjectScriptService implements Service {
   
   private final ConnectListener script;
   private final ProjectBuilder builder;
   private final ProcessManager engine;
   private final ConsoleLogger logger;
   
   public ProjectScriptService(ProcessManager engine, ConnectListener script, ConsoleLogger logger, ProjectBuilder builder) {
      this.script = script;
      this.builder = builder;
      this.logger = logger;
      this.engine = engine;
   }  
  
   public void connect(Session connection) {
      Request request = connection.getRequest();    
      Path path = request.getPath(); // /connect/<project-name>
      
      try {
         FrameChannel frameChannel = connection.getChannel();
         Project project = builder.createProject(path);
         File projectPath = project.getProjectPath();
         String projectName = project.getProjectName();
         Channel channel = request.getChannel();
         Socket socket = channel.getSocket().socket();
         int port = socket.getLocalPort();
         String name = String.valueOf(port);
         
         try {
            CommandListener listener = new CommandListener(engine, frameChannel, logger, projectPath, projectName);
            CommandController controller = new CommandController(listener);
            
            frameChannel.register(controller);
            script.connect(listener, path); // if there is a script then execute it
         } catch(Exception e) {
            logger.log("Could not connect " + path, e);
         }
      }catch(Exception e){
         logger.log("Error connecting " + path, e);
      }
      
   }
}
