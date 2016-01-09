package org.snapscript.web.project;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.socket.FrameChannel;
import org.simpleframework.http.socket.Session;
import org.simpleframework.http.socket.service.Service;
import org.simpleframework.transport.Channel;
import org.snapscript.web.WebScriptEngine;
import org.snapscript.web.WebScriptMessageRouter;

public class ProjectScriptService implements Service {
   
   private final WebScriptMessageRouter router;
   private final WebScriptEngine engine;
   private final File rootPath;
   
   public ProjectScriptService(WebScriptEngine engine, File rootPath) {
      this.router = new WebScriptMessageRouter();
      this.rootPath = rootPath;
      this.engine = engine;
      
      engine.register(router); // register for messages from script agents
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
         int port = socket.getPort();
         String agentName = String.format("agent-%s-%s", projectName, port);
         
         try {
            File projectPath = new File(rootPath, projectName);
            ProjectScriptController controller = new ProjectScriptController(engine, projectPath, projectName, agentName);
            
            frameChannel.register(controller);
            router.join(agentName, frameChannel);
         } catch(Exception e) {
            e.printStackTrace();
            router.leave(agentName);
         }
      }catch(Exception e){
         e.printStackTrace();
      }
      
   }
}
