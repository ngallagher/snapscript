package org.snapscript.engine.command;

import java.io.File;

import org.simpleframework.http.socket.Frame;
import org.simpleframework.http.socket.FrameChannel;
import org.simpleframework.http.socket.FrameListener;
import org.simpleframework.http.socket.FrameType;
import org.simpleframework.http.socket.Reason;
import org.simpleframework.http.socket.Session;
import org.snapscript.engine.ExceptionBuilder;
import org.snapscript.engine.agent.ProcessEngine;

public class CommandController implements FrameListener {

   private final CommandListener listener;
   private final CommandReader reader;
   
   public CommandController(ProcessEngine engine, FrameChannel channel, File root, String project) {
      this.listener = new CommandListener(engine, channel, root, project);
      this.reader = new CommandReader();
   }

   @Override
   public void onFrame(Session socket, Frame frame) {
      FrameType type = frame.getType();

      if(type == FrameType.TEXT){
         String text = frame.getText();

         try {
            Command command = reader.read(text);
            
            if(command instanceof ExecuteCommand) {
               listener.onExecute((ExecuteCommand)command);
            } else if(command instanceof SuspendCommand) {
               listener.onSuspend((SuspendCommand)command);
            }else if(command instanceof DeleteCommand) {
               listener.onDelete((DeleteCommand)command);
            }else if(command instanceof SaveCommand) {
               listener.onSave((SaveCommand)command);
            }
         } catch(Throwable e){
            e.printStackTrace();
         }
      } 
      System.err.println("onFrame(" + type + ")");
   }

   @Override
   public void onError(Session socket, Exception cause) {
      System.err.println("onError(" + ExceptionBuilder.build(cause) + ")");
   }

   @Override
   public void onClose(Session session, Reason reason) {
      System.err.println("onClose(" + reason + ")");
   }

}
