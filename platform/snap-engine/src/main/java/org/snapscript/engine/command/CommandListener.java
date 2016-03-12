package org.snapscript.engine.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.simpleframework.http.socket.FrameChannel;
import org.snapscript.engine.ProcessEngine;
import org.snapscript.engine.http.project.ProjectScriptValidator;

public class CommandListener {
   
   private final ProjectScriptValidator validator;
   private final CommandEventForwarder forwarder;
   private final CommandFilter filter;
   private final CommandClient client;
   private final ProcessEngine engine;
   private final String project;
   private final String name;
   private final File root;
   
   public CommandListener(ProcessEngine engine, FrameChannel channel, File root, String project, String name) {
      this.filter = new CommandFilter();
      this.client = new CommandClient(channel, project);
      this.forwarder = new CommandEventForwarder(client, filter);
      this.validator = new ProjectScriptValidator();
      this.project = project;
      this.engine = engine;
      this.name = name;
      this.root = root;
   }

   public void onSave(SaveCommand command) {
      try {
         String resource = command.getResource();
         String source = command.getSource();
         //int line = validator.parse(resource, source);
         
         if(!command.isDirectory()) {
            File file = new File(root, "/" + resource);
            boolean exists = file.exists();
            
            if(command.isCreate() && exists) {
               client.sendAlert(resource, "Resource " + resource + " already exists");
            } else {
               FileOutputStream out = new FileOutputStream(file);
               OutputStreamWriter encoder = new OutputStreamWriter(out, "UTF-8");
               encoder.write(source);
               encoder.close();
               
               if(!exists) {
                  client.sendReloadTree();
               }
            }
         } else {
            File file = new File(root, "/"+resource);
            
            if(!file.exists()) {
               file.mkdirs();
               client.sendReloadTree();
            }
         }
         //} else {
         //   client.sendSyntaxError(resource, line);
         //}
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public void onExecute(ExecuteCommand command) {
      try {
         String resource = command.getResource();
         String source = command.getSource();
         int line = validator.parse(resource, source);
         
         if(line == -1) {
            File file = new File(root, "/" + resource);
            FileOutputStream out = new FileOutputStream(file);
            OutputStreamWriter encoder = new OutputStreamWriter(out, "UTF-8");
            encoder.write(source);
            encoder.close();
            //client.sendReloadTree();
            
            engine.register(forwarder); // make sure we are registered
            engine.execute(command, filter); 
         } else {
            client.sendSyntaxError(resource, line);
         }
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public void onAttach(AttachCommand command) {
      try {
         String process = command.getProcess();
         String focus = filter.get();
         
         if(focus == null) { // not focused
            if(command.isFocus()) {
               filter.attach(process);
            }
         } else if(process.equals(focus)) { // focused
            if(command.isFocus()) {
               filter.attach(process); // accept messages from this process
            } else {
               filter.clear(); // clear the focus
            }
         } else {
            if(command.isFocus()) {
               filter.attach(process);
            }
         }
         engine.breakpoints(command, process);
         engine.register(forwarder); // make sure we are registered
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public void onStep(StepCommand command) {
      try {
         String focus = filter.get();
         
         if(focus != null) {
            engine.step(command, focus);
         }
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public void onDelete(DeleteCommand command) {
      try {
         String resource = command.getResource();
         File file = new File(root, "/" + resource);
         
         if(file.exists()) {
            file.delete();
            client.sendReloadTree();
         }
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public void onBreakpoints(BreakpointsCommand command) {
      try {
         String focus = filter.get();
         
         if(focus != null) {
            engine.breakpoints(command, focus);
         }
      } catch(Exception e){
         e.printStackTrace();
      }
   }
   
   public void onBrowse(BrowseCommand command) {
      try {
         String focus = filter.get();
         
         if(focus != null) {
            engine.browse(command, focus);
         }
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public void onStop(StopCommand command) {
      try {
         String focus = filter.get();
         
         if(focus != null) {
            engine.stop(focus);
            client.sendProcessTerminate(focus);
            filter.clear();
         }
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public void onPing() {
      try {
         String focus = filter.get();
         
         if(focus != null) {
            if(!engine.ping(focus)) {
               client.sendProcessTerminate(focus);
               filter.clear();
            }
         }
         engine.register(forwarder); // make sure we are registered
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public void onClose() {
      try {
         //client.sendProcessTerminate();
         engine.remove(forwarder);
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
}
