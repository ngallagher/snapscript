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

import com.google.gson.Gson;

public class CommandController implements FrameListener {
   
   private static final String SAVE_COMMAND = "SAVE";
   private static final String SUSPEND_COMMAND = "SUSPEND";
   private static final String DELETE_COMMAND = "DELETE";
   private static final String EXECUTE_COMMAAND = "EXECUTE";
   
   private final CommandListener listener;
   private final Gson gson;
   
   public CommandController(ProcessEngine engine, FrameChannel channel, File root, String project) {
      this.listener = new CommandListener(engine, channel, root, project);
      this.gson = new Gson();
   }

   @Override
   public void onFrame(Session socket, Frame frame) {
      FrameType type = frame.getType();

      if(type == FrameType.TEXT){
         String text = frame.getText();
         
         try {
            if(text.startsWith(EXECUTE_COMMAAND)) {
               String value = text.substring(EXECUTE_COMMAAND.length() + 1);
               ExecuteCommand command = gson.fromJson(value, ExecuteCommand.class);
               listener.onExecute(command);
            } else if(text.startsWith(SUSPEND_COMMAND)) {
               String value = text.substring(SUSPEND_COMMAND.length() + 1);
               SuspendCommand command = gson.fromJson(value, SuspendCommand.class);
               listener.onSuspend(command);
            }else if(text.startsWith(DELETE_COMMAND)) {
               String value = text.substring(DELETE_COMMAND.length() + 1);
               listener.onDelete(null);
            }else if(text.startsWith(SAVE_COMMAND)) {
               String value = text.substring(SAVE_COMMAND.length() + 1);
               SaveCommand command = gson.fromJson(value, SaveCommand.class);
               listener.onSave(command);
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
