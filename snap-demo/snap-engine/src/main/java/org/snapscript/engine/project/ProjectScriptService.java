package org.snapscript.engine.project;

import java.io.File;
import java.net.Socket;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.socket.FrameChannel;
import org.simpleframework.http.socket.Session;
import org.simpleframework.http.socket.service.Service;
import org.simpleframework.transport.Channel;
import org.snapscript.engine.agent.ProcessEngine;
import org.snapscript.engine.command.CommandController;

public class ProjectScriptService implements Service {
   
   private final ProcessEngine engine;
   private final File rootPath;
   
   public ProjectScriptService(ProcessEngine engine, File rootPath) {
      this.rootPath = rootPath;
      this.engine = engine;
   }  
  
   public void connect(Session connection) {
      try {
         FrameChannel frameChannel = connection.getChannel();
         Request request = connection.getRequest();    
         Path path = request.getPath(); // /connect/<project-name>
         String projectPrefix = path.getPath(1, 2); // /<project-name>
         String projectName = projectPrefix.substring(1); // <project-name>
         Channel channel = request.getChannel();
         Socket socket = channel.getSocket().socket();
         int port = socket.getLocalPort();
         String name = String.valueOf(port);
         
         try {
            File projectPath = new File(rootPath, projectName);
            CommandController controller = new CommandController(engine, frameChannel, projectPath, projectName, name);
            
            frameChannel.register(controller);
         } catch(Exception e) {
            e.printStackTrace();
         }
      }catch(Exception e){
         e.printStackTrace();
      }
      
   }
}
