package org.snapscript.engine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;

import org.simpleframework.http.Path;
import org.snapscript.engine.command.CommandListener;
import org.snapscript.engine.command.ExecuteCommand;
import org.snapscript.engine.http.project.Project;
import org.snapscript.engine.http.project.ProjectBuilder;

// 1) start engine container on ephemeral port
// 2) ensure stdout/stderr goes to both client and console
// 3) once server is running launch the agent with agent-pool=0
// 4) as soon as the agent is running, issue an ExecuteCommand ---- should be the same process11!
// 5) once connected the process should suspend
public class ProcessEngineScript {

   private final ProjectBuilder builder;
   
   public ProcessEngineScript(ProjectBuilder builder) {
      this.builder = builder;
   }
   
   public void connect(CommandListener listener, Path path) {
      String script = ProcessEngineArgument.SCRIPT.getValue();
      
      if(script != null) {
         try {
            Project project = builder.createProject(path);
            File projectPath = project.getProjectPath();
            String projectName = project.getProjectName();
            File file = new File(projectPath, "/" + script);
            FileInputStream input = new FileInputStream(file);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] chunk = new byte[1024];
            int count = 0;
            
            while((count = input.read(chunk))!=-1) {
               buffer.write(chunk, 0, count);
            }
            input.close();
            buffer.close();
            
            String source = buffer.toString("UTF-8");
            String system = System.getProperty("os.name");
            ExecuteCommand command = new ExecuteCommand(projectName, system, script, source, Collections.EMPTY_MAP);
            
            listener.onExecute(command);
         } catch(Exception e) {
            e.printStackTrace();
         }
      }
   }
}
