package org.snapscript.develop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.snapscript.agent.ConsoleLogger;

public class BackupManager {
   
   private static final String BACKUP_FOLDER = ".backup";
   private static final String DATE_FORMAT = "yyyyMMddHHmmssSSS";
   
   private final ConsoleLogger logger;
   private final Workspace workspace;
   private final DateFormat format;
   
   public BackupManager(ConsoleLogger logger,Workspace workspace) {
      this.format = new SimpleDateFormat(DATE_FORMAT);
      this.workspace = workspace;
      this.logger = logger;
   }
   
   public synchronized void backup(File root, File file, String project) {
      if(file.isFile()) {
         File backupRoot = workspace.create(BACKUP_FOLDER);
         long time = System.currentTimeMillis();
         String extension = format.format(time);
         String relative = relative(root, file);
         String timestampFile = String.format("%s/%s.%s", project, relative, extension);
         File backupFile = new File(backupRoot, timestampFile);
         File backupDirectory = backupFile.getParentFile();
         
         if(!backupDirectory.exists()) {
            backupDirectory.mkdirs();
         }
         copy(file, backupFile);
      } else {
         File[] files = file.listFiles();
         
         for(File entry : files) {
            backup(root, entry, project);
         }
      }
   }
   
   public synchronized void copy(File from, File to) {
      try {
         FileInputStream input = new FileInputStream(from);
         FileOutputStream output = new FileOutputStream(to);
         byte[] buffer = new byte[1024];
         int count = 0;
         
         while((count = input.read(buffer))!=-1){
            output.write(buffer, 0, count);
         }
         input.close();
         output.close();
      } catch(Exception e) {
         logger.log("Could not backup " + from + " to " + to);
      }
   }
   
   public synchronized void delete(File file) {
      try {
         if(file.exists()) {
            if(file.isDirectory()) {
               File[] files = file.listFiles();
               
               for(File entry : files) {
                  if(entry.isDirectory()) {
                     delete(entry);
                  } else {
                     if(entry.exists()) {
                        entry.delete();
                     }
                  }
               }
            } else {
               file.delete();
            }
         }
      } catch(Exception e) {
         logger.log("Could not delete " + file);
      }
   }
   
   public synchronized void save(File file, String content) {
      try {
         FileOutputStream out = new FileOutputStream(file);
         OutputStreamWriter encoder = new OutputStreamWriter(out, "UTF-8");
         
         encoder.write(content);
         encoder.close();
      } catch(Exception e) {
         logger.log("Could not save " + file);
      }
   }
   
   private synchronized String relative(File root, File file) {
      return root.toURI().relativize(file.toURI()).getPath();
   }
}
