package org.snapscript.develop.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.simpleframework.http.Path;
import org.simpleframework.http.socket.FrameChannel;
import org.snapscript.agent.ConsoleLogger;
import org.snapscript.develop.BackupManager;
import org.snapscript.develop.ProcessManager;
import org.snapscript.develop.common.Problem;
import org.snapscript.develop.common.ProblemFinder;
import org.snapscript.develop.common.TypeNode;
import org.snapscript.develop.complete.TypeNodeScanner;
import org.snapscript.develop.http.project.ProjectProblemFinder;

public class CommandListener {
   
   private final CommandEventForwarder forwarder;
   private final ProjectProblemFinder compiler;
   private final TypeNodeScanner loader;
   private final CommandFilter filter;
   private final CommandClient client;
   private final ProcessManager engine;
   private final ConsoleLogger logger;
   private final ProblemFinder finder;
   private final BackupManager manager;
   private final String project;
   private final File root;
   private final Path path;
   
   public CommandListener(ProcessManager engine, ProjectProblemFinder compiler, TypeNodeScanner loader, FrameChannel channel, ConsoleLogger logger, BackupManager manager, Path path, File root, String project) {
      this.filter = new CommandFilter();
      this.client = new CommandClient(channel, project);
      this.forwarder = new CommandEventForwarder(client, filter);
      this.finder = new ProblemFinder();
      this.compiler = compiler;
      this.manager = manager;
      this.loader = loader;
      this.logger = logger;
      this.engine = engine;
      this.project = project;
      this.root = root;
      this.path = path;
   }

   public void onExplore(ExploreCommand command) {
      String resource = command.getResource();
      
      try {
         if(resource != null) {
            File file = new File(root, "/" + resource);
            
            if(file.isDirectory()) {
               file = file.getParentFile();
            }
            String path = file.getCanonicalPath();
            boolean exists = file.exists();
            boolean directory = file.isDirectory();
            
            if(exists && directory) {
               List<String> arguments = new ArrayList<String>();
               
               arguments.add("explorer");
               arguments.add(path);

               ProcessBuilder builder = new ProcessBuilder(arguments);
               builder.start();
            }
         }
      } catch(Exception e) {
         logger.log("Error exploring directory " + resource, e);
      }
   }
   
   public void onSave(SaveCommand command) {
      String resource = command.getResource();
      String source = command.getSource();
      
      try {
         if(!command.isDirectory()) {
            Problem problem = finder.parse(project, resource, source);
            File file = new File(root, "/" + resource);
            boolean exists = file.exists();
            
            if(exists) {
               manager.backupFile(root, file, project);
            }
            if(command.isCreate() && exists) {
               client.sendAlert(resource, "Resource " + resource + " already exists");
            } else {
               manager.saveFile(file, source);
               
               if(problem == null) {
                  client.sendSyntaxError(resource, "", 0, -1); // clear problem
               } else {
                  String description = problem.getDescription();
                  int line = problem.getLine();
                  long time = System.currentTimeMillis();
                  
                  client.sendSyntaxError(resource, description, time, line);
               }
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
      } catch(Exception e) {
         logger.log("Error saving " + resource, e);
      }
   }
   
   public void onRename(RenameCommand command) {
      String from = command.getFrom();
      String to = command.getTo();
      
      try {
         File fromFile = new File(root, "/" + from);
         File toFile = new File(root, "/" + to); 
         
         if(!fromFile.equals(root)) { // don't rename root
            boolean fromExists = fromFile.exists();
            boolean toExists = toFile.exists();
            
            if(!fromExists) {
               client.sendAlert(from, "Resource " + from + " does not exist");
            } else {
               if(toExists) {
                  client.sendAlert(to, "Resource " + to + " does already exists");
               } else {
                  if(fromFile.renameTo(toFile)){
                     client.sendReloadTree();
                  } else {
                     client.sendAlert(from, "Could not rename " + from + " to " + to);
                  }
               }
            }
         } 
      } catch(Exception e) {
         logger.log("Error renaming " + from, e);
      }
   }   
   
   public void onExecute(ExecuteCommand command) {
      String resource = command.getResource();
      String source = command.getSource();
      
      try {
         Problem problem = finder.parse(project, resource, source);
         
         if(problem == null) {
            File file = new File(root, "/" + resource);
            boolean exists = file.exists();
            
            if(exists) {
               manager.backupFile(root, file, project);
            }
            manager.saveFile(file, source);
            client.sendSyntaxError(resource, "", 0, -1); // clear problem
            engine.register(forwarder); // make sure we are registered
            engine.execute(command, filter); 
         } else {
            String description = problem.getDescription();
            int line = problem.getLine();
            long time = System.currentTimeMillis();
            
            client.sendSyntaxError(resource, description, time, line);
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
         
         if(!file.equals(root)) { // don't delete root
            boolean exists = file.exists();
            
            if(exists) {
               manager.backupFile(root, file, project);
               
               if(file.isDirectory()) {
                  
               }
               file.delete();
               client.sendReloadTree();
            }
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
         Set<Problem> problems = compiler.compileProject(path);
//         Map<String, TypeNode> nodes = loader.compileProject(path);
//         Set<String> names = nodes.keySet();
//         int index = 0;
//         
//         for(String name : names) {
//            String[] pair = name.split(":");
//            TypeNode node = nodes.get(name);
//            index++;
//            
//            if(node.isModule()) {
//               logger.log(index + " module " + pair[0] + " --> '" + pair[1] + "'");
//            } else {
//               logger.log(index + " class " + pair[0] + " --> '" + pair[1] + "'");
//            }
//         }
         for(Problem problem : problems) {
            String description = problem.getDescription();
            String path = problem.getResource();
            int line = problem.getLine();
            long time = System.currentTimeMillis();
            
            client.sendSyntaxError(path,description,  time, line);
         }
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
