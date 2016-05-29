package org.snapscript.core.extend;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Context;

public class FileExtension {
   
   private final Context context;
   
   public FileExtension(Context context) {
      this.context = context;
   }

   public List<File> find(File directory, String pattern) throws IOException {
      List<File> files = new ArrayList<File>();
      
      if(directory.exists()) {
         File[] list = directory.listFiles();
         
         if(list != null) {
            for(File file : list) {
               File normal = file.getCanonicalFile();
               String name = normal.getName();
               
               if(name.matches(pattern)) {
                  files.add(normal);
               }
               if(file.isDirectory()) {
                  List<File> children = find(normal, pattern);
                  
                  if(!children.isEmpty()) {
                     files.addAll(children);
                  }
               }
            }
         }
      }
      return files;
   }
   
   public List<File> find(File directory, FileFilter filter) throws IOException {
      List<File> files = new ArrayList<File>();
      
      if(directory.exists()) {
         File[] list = directory.listFiles();
         
         if(list != null) {
            for(File file : list) {
               File normal = file.getCanonicalFile();
               
               if(filter.accept(normal)) {
                  files.add(normal);
               }
               if(file.isDirectory()) {
                  List<File> children = find(normal, filter);
                  
                  if(!children.isEmpty()) {
                     files.addAll(children);
                  }
               }
            }
         }
      }
      return files;
   }
}
