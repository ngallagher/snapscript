package org.snapscript.engine.http.project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.simpleframework.http.Path;

public class ProjectBuilder {
   
   private static final String DEFAULT_PROJECT = "default";
   
   private final Map<String, Project> projects;
   private final ProjectMode mode;
   private final Project single;
   private final File workPath;
   
   public ProjectBuilder(ProjectMode mode, File workPath){
      this.projects = new ConcurrentHashMap<String, Project>();
      this.single = new Project(workPath, ".", DEFAULT_PROJECT);
      this.workPath = workPath;
      this.mode = mode;
   }
   
   public File getRoot() {
      return workPath;
   }
   
   public Project createProject(Path path){ // /project/<project-name>/ || /project/default
      if(mode.isMultipleMode()) { // multiple project support
         String projectPrefix = path.getPath(1, 1); // /<project-name>
         String projectName = projectPrefix.substring(1); // <project-name>
         Project project = projects.get(projectName);
         
         if(project == null) {
            project = new Project(workPath, projectName, projectName);
            projects.put(projectName, project);
         }
         File file = project.getProjectPath();
         
         if(!file.exists()) {
            file.mkdirs();
            createDefaultProject(file);
         }
         return project;
      }
      return single;
   }
   
   private void createDefaultProject(File file) {
      try {
         File ignore = new File(file, ".gitignore");
         OutputStream stream = new FileOutputStream(ignore);
         PrintStream print = new PrintStream(stream);
         print.println("/temp/");
         print.close();
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
}
