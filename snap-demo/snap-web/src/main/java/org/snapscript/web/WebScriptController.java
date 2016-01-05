package org.snapscript.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.simpleframework.http.socket.Frame;
import org.simpleframework.http.socket.FrameListener;
import org.simpleframework.http.socket.FrameType;
import org.simpleframework.http.socket.Reason;
import org.simpleframework.http.socket.Session;

public class WebScriptController implements FrameListener {

   private final WebScriptEngine engine;
   private final File tempPath;
   private final String agent;
   
   public WebScriptController(WebScriptEngine engine, File tempPath, String agent) {
      this.engine = engine;
      this.tempPath = tempPath;
      this.agent = agent;
   }

   public void onFrame(Session socket, Frame frame) {
      FrameType type = frame.getType();
      String text = frame.getText();
      
      if(type == FrameType.TEXT){
         try {
            File scriptDir = new File(tempPath, "temp"); // only one command, send the script to run
            
            if(!scriptDir.exists()) {
               scriptDir.mkdirs();
            }
            File file = new File(scriptDir, "temp"+System.currentTimeMillis()+".snap");
            FileOutputStream out = new FileOutputStream(file);
            OutputStreamWriter encoder = new OutputStreamWriter(out, "UTF-8");
            encoder.write(text);
            encoder.close();
            engine.executeScript(file, agent, System.getProperty("os.name"));
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
