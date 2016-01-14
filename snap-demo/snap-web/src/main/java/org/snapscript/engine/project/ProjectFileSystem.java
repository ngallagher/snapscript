package org.snapscript.engine.project;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ProjectFileSystem {

   private final Project project;
   
   public ProjectFileSystem(Project project) {
      this.project = project;
   }
   
   public void writeAsByteArray(String path, String resource) throws Exception {
      byte[] octets = resource.getBytes("UTF-8");
      writeAsByteArray(path, octets);
   }
   
   public void writeAsByteArray(String path, byte[] resource) throws Exception {
      File sourcePath = project.getSourcePath();
      File tempPath = project.getTempPath();
      
      if(!tempPath.exists()) {
         tempPath.mkdirs();
      }
      String realPath = path.replace('/', File.separatorChar);
      File sourceFile = new File(sourcePath, realPath);
      FileOutputStream outputStream = new FileOutputStream(sourceFile);
      outputStream.write(resource);
      outputStream.close();
   }
   
   public String readAsString(String path) throws Exception {
      byte[] resource = readAsByteArray(path);
      return new String(resource, "UTF-8");
   }
   
   public byte[] readAsByteArray(String path) throws Exception {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      File sourcePath = project.getSourcePath();
      File tempPath = project.getTempPath();
      File rootPath = project.getProjectPath();
      
      if(!tempPath.exists()) {
         tempPath.mkdirs();
      }
      String realPath = path.replace('/', File.separatorChar);
      File sourceFile = new File(sourcePath, realPath);
      File tempFile = new File(tempPath, realPath);
      File projectFile = new File(rootPath, realPath);
      InputStream inputStream = null;
      
      if(sourceFile.exists()) {
         inputStream = new FileInputStream(sourceFile);
      } else if(tempFile.exists()) {
         inputStream = new FileInputStream(tempFile);
      } else if(projectFile.exists()) {
         inputStream = new FileInputStream(projectFile);
      } else {
         inputStream = ProjectFileResource.class.getResourceAsStream(path);
      }
      byte[] chunk = new byte[1024];
      int count = 0;

      while((count = inputStream.read(chunk)) != -1) {
         buffer.write(chunk, 0, count);
      }
      inputStream.close();
      return buffer.toByteArray();
   }
   
}
