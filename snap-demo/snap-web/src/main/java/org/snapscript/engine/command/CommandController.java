package org.snapscript.engine.command;

import java.io.File;

import org.simpleframework.http.socket.Frame;
import org.simpleframework.http.socket.FrameChannel;
import org.simpleframework.http.socket.FrameListener;
import org.simpleframework.http.socket.FrameType;
import org.simpleframework.http.socket.Reason;
import org.simpleframework.http.socket.Session;
import org.snapscript.engine.agent.ProcessEngine;

public class CommandController implements FrameListener {

   private final CommandListener listener;
   private final CommandReader reader;
   
   public CommandController(ProcessEngine engine, FrameChannel channel, File root, String project, String name) {
      this.listener = new CommandListener(engine, channel, root, project, name);
      this.reader = new CommandReader();
   }

   @Override
   public void onFrame(Session socket, Frame frame) {
      FrameType type = frame.getType();

      try {
         if(type == FrameType.TEXT){
            String text = frame.getText();
            Command command = reader.read(text);
            
            if(command instanceof ExecuteCommand) {
               listener.onExecute((ExecuteCommand)command);
            } else if(command instanceof BreakpointsCommand) {
               listener.onBreakpoints((BreakpointsCommand)command);
            }else if(command instanceof DeleteCommand) {
               listener.onDelete((DeleteCommand)command);
            }else if(command instanceof SaveCommand) {
               listener.onSave((SaveCommand)command);
            }else if(command instanceof StepCommand) {
               listener.onStep((StepCommand)command);
            }else if(command instanceof StopCommand) {
               listener.onStop((StopCommand)command);
            }
         } else if(type == FrameType.PONG){
            listener.onPing();
         }
      } catch(Throwable e){
         e.printStackTrace();
      }
   }

   @Override
   public void onError(Session socket, Exception cause) {
      cause.printStackTrace();
      listener.onClose();
   }

   @Override
   public void onClose(Session session, Reason reason) {
      listener.onClose();
   }

}
