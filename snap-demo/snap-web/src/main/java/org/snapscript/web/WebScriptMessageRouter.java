package org.snapscript.web;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.simpleframework.http.socket.FrameChannel;
import org.snapscript.web.message.Message;
import org.snapscript.web.message.MessageListener;
import org.snapscript.web.message.MessageType;

public class WebScriptMessageRouter implements MessageListener {

   private final Map<String, FrameChannel> sockets;
   private final Set<String> agents;
   
   public WebScriptMessageRouter() {
      this.sockets = new ConcurrentHashMap<String, FrameChannel>();
      this.agents = new CopyOnWriteArraySet<String>();
   } 
   
   public void join(String agent, FrameChannel operation) {
      sockets.put(agent, operation);
      agents.add(agent);
   }
   
   public void leave(String agent){
      sockets.remove(agent);
      agents.remove(agent);
   }

   @Override
   public void onMessage(Message message) {
      try {          
         String processId = message.getProcessId();
         FrameChannel operation = sockets.get(processId);
         
         try {               
            if(operation != null) {
               MessageType type = message.getType();
               String text = message.getData("UTF-8");
               operation.send(type.prefix+""+type+":"+text);
            }
         } catch(Exception e){   
            sockets.remove(processId);
            agents.remove(processId);
            operation.close();
            e.printStackTrace();
         }
      } catch(Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   public void onError(Exception cause) {
      System.err.println("onError(" + ExceptionBuilder.build(cause) + ")");
   }

   @Override
   public void onClose() {
      System.err.println("onClose()");
   }
}
