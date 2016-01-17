package org.snapscript.engine.http.project;

import java.io.File;

public class Project {

   private static final String SOURCE_PATH = "src";
   private static final String TEMP_PATH = "temp";
   
   private final ProjectFileSystem fileSystem;
   private final File tempPath;
   private final File sourcePath;
   private final File projectPath;
   private final String projectName;
   
   public Project(File rootPath, String projectName){
      this.fileSystem = new ProjectFileSystem(this);
      this.projectPath = new File(rootPath, projectName);
      this.sourcePath = new File(projectPath, SOURCE_PATH);
      this.tempPath = new File(projectPath, TEMP_PATH);
      this.projectName = projectName;
   }
   
   public ProjectFileSystem getFileSystem() {
      return fileSystem;
   }

   public File getTempPath() {
      return tempPath;
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
