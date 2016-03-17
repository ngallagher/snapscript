package org.snapscript.engine.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.simpleframework.http.socket.FrameChannel;
import org.snapscript.agent.ConsoleLogger;
import org.snapscript.engine.ProcessManager;
import org.snapscript.engine.http.project.ProjectScriptValidator;

public class CommandListener {
   
   private final ProjectScriptValidator validator;
   private final CommandEventForwarder forwarder;
   private final CommandFilter filter;
   private final CommandClient client;
   private final ProcessManager engine;
   private final ConsoleLogger logger;
   private final String project;
   private final File root;
   
   public CommandListener(ProcessManager engine, FrameChannel channel, ConsoleLogger logger, File root, String project) {
      this.filter = new CommandFilter();
      this.client = new CommandClient(channel, project);
      this.forwarder = new CommandEventForwarder(client, filter);
      this.validator = new ProjectScriptValidator();
      this.logger = logger;
      this.engine = engine;
      this.project = project;
      this.root = root;
   }

   public void onSave(SaveCommand command) {
      String resource = command.getResource();
      String source = command.getSource();
      
      try {
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
         logger.log("Error saving " + resource, e);
      }
   }
   
   public void onExecute(ExecuteCommand command) {
      String resource = command.getResource();
      String source = command.getSource();
      
      try {
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
         logger.log("Error executing " + resource, e);
      }
   }
   
   public void onAttach(AttachCommand command) {
      String process = command.getProcess();
      
      try {
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
         logger.log("Error attaching to process " + process, e);
      }
   }
   
   public void onStep(StepCommand command) {
      String thread = command.getThread();
      String focus = filter.get();
            
      try {
         if(focus != null) {
            engine.step(command, focus);
         }
      } catch(Exception e) {
         logger.log("Error stepping through " + thread +" in process " + focus, e);
      }
   }
   
   public void onDelete(DeleteCommand command) {
      String resource = command.getResource();
      
      try {
         File file = new File(root, "/" + resource);
         
         if(file.exists()) {
            file.delete();
            client.sendReloadTree();
         }
      } catch(Exception e) {
         logger.log("Error deleting " + resource, e);
      }
   }
   
   public void onBreakpoints(BreakpointsCommand command) {
      String focus = filter.get();
      
      try {
         if(focus != null) {
            engine.breakpoints(command, focus);
         }
      } catch(Exception e){
         logger.log("Error setting breakpoints for process " + focus, e);
      }
   }
   
   public void onBrowse(BrowseCommand command) {
      String focus = filter.get();
      
      try {
         if(focus != null) {
            engine.browse(command, focus);
         }
      } catch(Exception e) {
         logger.log("Error browsing variables for process " + focus, e);
      }
   }
   
   public void onStop(StopCommand command) {
      String focus = filter.get();
      
      try {
         if(focus != null) {
            engine.stop(focus);
            client.sendProcessTerminate(focus);
            filter.clear();
         }
      } catch(Exception e) {
         logger.log("Error stopping process " + focus, e);
      }
   }
   
   public void onPing() {
      String focus = filter.get();
      
      try {
         if(focus != null) {
            if(!engine.ping(focus)) {
               client.sendProcessTerminate(focus);
               filter.clear();
            }
         }
         engine.register(forwarder); // make sure we are registered
      } catch(Exception e) {
         logger.log("Error pinging process " + focus, e);
      }
   }
   
   public void onClose() {
      try {
         //client.sendProcessTerminate();
         engine.remove(forwarder);
      } catch(Exception e) {
         logger.log("Error removing listener", e);
      }
   }
}
