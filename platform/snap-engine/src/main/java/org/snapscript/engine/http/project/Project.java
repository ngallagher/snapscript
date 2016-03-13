package org.snapscript.engine.http.project;

import java.io.File;

public class Project {
   
   private final ProjectFileSystem fileSystem;
   private final File sourcePath;
   private final File projectPath;
   private final String projectName;
   private final String projectDirectory;

   public Project(File rootPath, String projectDirectory, String projectName) {
      this.fileSystem = new ProjectFileSystem(this);
      this.projectPath = new File(rootPath, projectDirectory);
      this.projectDirectory = projectDirectory;
      this.sourcePath = projectPath;
      this.projectName = projectName;
   }

   public ProjectFileSystem getFileSystem() {
      return fileSystem;
   }

   public File getSourcePath() {
      try {
         return sourcePath.getCanonicalFile();
      } catch (Exception e) {
         throw new IllegalStateException("Could not get source path for '" + sourcePath + "'");
      }
   }

   public File getProjectPath() {
      try {
         return projectPath.getCanonicalFile();
      } catch (Exception e) {
         throw new IllegalStateException("Could not get project path for '" + projectPath + "'");
      }
   }
   
   public String getProjectDirectory() {
      return projectDirectory;
   }

   public String getProjectName() {
      return projectName;
   }
}
