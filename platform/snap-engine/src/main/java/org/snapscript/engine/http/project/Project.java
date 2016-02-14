package org.snapscript.engine.http.project;

import java.io.File;

public class Project {
   
   private final ProjectFileSystem fileSystem;
   private final File sourcePath;
   private final File projectPath;
   private final String projectName;
   
   public Project(File rootPath, String projectName){
      this.fileSystem = new ProjectFileSystem(this);
      this.projectPath = new File(rootPath, projectName);
      this.projectName = projectName;
      this.sourcePath = projectPath;
   }
   
   public ProjectFileSystem getFileSystem() {
      return fileSystem;
   }

   public File getSourcePath() {
      return sourcePath;
   }

   public File getProjectPath() {
      return projectPath;
   }

   public String getProjectName() {
      return projectName;
   }
   
   
}
