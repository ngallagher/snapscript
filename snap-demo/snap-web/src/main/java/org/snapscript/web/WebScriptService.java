package org.snapscript.web;

import java.io.File;

import org.simpleframework.http.Request;
import org.simpleframework.http.socket.FrameChannel;
import org.simpleframework.http.socket.Session;
import org.simpleframework.http.socket.service.Service;

public class WebScriptService implements Service {
   
   private final WebScriptEngine engine;
   private final WebScriptMessageRouter router;
   private final File tempPath;
   
   public WebScriptService(WebScriptEngine engine, File tempPath) {
      this.router = new WebScriptMessageRouter();
      this.tempPath = tempPath;
      this.engine = engine;
      
      engine.register(router); // register for messages from script agents
   }  
  
   public void connect(Session connection) {
      try {
         FrameChannel socket = connection.getChannel();
         Request req = connection.getRequest();      
         String channelAddress = req.getChannel().getSocket().toString();
         
         try {
            WebScriptController controller = new WebScriptController(engine, tempPath, channelAddress);
            socket.register(controller);
            router.join(channelAddress, socket);
         } catch(Exception e) {
            e.printStackTrace();
            router.leave(channelAddress);
         }
      }catch(Exception e){
         e.printStackTrace();
      }
      
   }
}
