package org.snapscript.engine.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.simpleframework.http.socket.FrameChannel;
import org.snapscript.engine.agent.ProcessEngine;
import org.snapscript.engine.project.ProjectScriptValidator;

public class CommandListener {
   
   private final ProjectScriptValidator validator;
   private final CommandEventForwarder forwarder;
   private final CommandClient client;
   private final ProcessEngine engine;
   private final String project;
   private final File root;
   
   public CommandListener(ProcessEngine engine, FrameChannel channel, File root, String project) {
      this.client = new CommandClient(channel, project);
      this.forwarder = new CommandEventForwarder(client);
      this.validator = new ProjectScriptValidator();
      this.project = project;
      this.engine = engine;
      this.root = root;
   }

   public void onSave(SaveCommand command) {
      try {
         String resource = command.getResource();
         String source = command.getSource();
         int line = validator.parse(resource, source);
         
         if(line == -1) {
            File file = new File(root, resource);
            FileOutputStream out = new FileOutputStream(file);
            OutputStreamWriter encoder = new OutputStreamWriter(out, "UTF-8");
            encoder.write(source);
            encoder.close();
            client.sendReloadTree();
         } else {
            client.sendSyntaxError(resource, line);
         }
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public void onExecute(ExecuteCommand command) {
      try {
         String system = System.getProperty("os.name");
         String resource = command.getResource();
         String source = command.getSource();
         int line = validator.parse(resource, source);
         
         if(line == -1) {
            File file = new File(root, resource);
            FileOutputStream out = new FileOutputStream(file);
            OutputStreamWriter encoder = new OutputStreamWriter(out, "UTF-8");
            encoder.write(source);
            encoder.close();
            //client.sendReloadTree();
            engine.execute(forwarder, project, resource, system);
         } else {
            client.sendSyntaxError(resource, line);
         }
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public void onDelete(DeleteCommand command) {
      
   }
   
   public void onSuspend(SuspendCommand command) {
      
   }
}
