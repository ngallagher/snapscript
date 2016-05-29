package org.snapscript.core.extend;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class FileExtension {
   
   public FileExtension() {
      super();
   }
   
   public List<String> contains(File file, String pattern) throws IOException {
      FileReader source = new FileReader(file);
      LineNumberReader reader = new LineNumberReader(source);
      List<String> lines = new ArrayList<String>();
      
      try {
         while(true) {
            String line = reader.readLine();
            
            if(line == null) {
               return lines;
            }
            if(line.contains(pattern)) {
               lines.add(line);
            }
         }
      } finally {
         reader.close();
      }
   }
   
   public List<String> search(File file, String pattern) throws IOException {
      FileReader source = new FileReader(file);
      LineNumberReader reader = new LineNumberReader(source);
      List<String> lines = new ArrayList<String>();
      
      try {
         while(true) {
            String line = reader.readLine();
            
            if(line == null) {
               return lines;
            }
            if(line.matches(pattern)) {
               lines.add(line);
            }
         }
      } finally {
         reader.close();
      }
   }
   
   public List<String> readLines(File file) throws IOException {
      FileReader reader = new FileReader(file);
      LineNumberReader iterator = new LineNumberReader(reader);
      List<String> lines = new ArrayList<String>();
      
      try {
         while(true) {
            String line = iterator.readLine();
            
            if(line == null) {
               return lines;
            }
            lines.add(line);
         }
      } finally {
         iterator.close();
      }
   }
   
   public List<String> readLines(File file, String encoding) throws IOException {
      InputStream stream = new FileInputStream(file);
      Reader reader = new InputStreamReader(stream, encoding);
      LineNumberReader iterator = new LineNumberReader(reader);
      List<String> lines = new ArrayList<String>();
      
      try {
         while(true) {
            String line = iterator.readLine();
            
            if(line == null) {
               return lines;
            }
            lines.add(line);
         }
      } finally {
         iterator.close();
      }
   }

   public List<File> findFiles(File directory, String pattern) throws IOException {
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
                  List<File> children = findFiles(normal, pattern);
                  
                  if(!children.isEmpty()) {
                     files.addAll(children);
                  }
               }
            }
         }
      }
      return files;
   }
   
   public List<File> findFiles(File directory, FileFilter filter) throws IOException {
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
                  List<File> children = findFiles(normal, filter);
                  
                  if(!children.isEmpty()) {
                     files.addAll(children);
                  }
               }
            }
         }
      }
      return files;
   }
   
   public List<String> findPaths(File directory, String pattern) throws IOException {
      List<String> paths = new ArrayList<String>();
      
      if(directory.exists()) {
         List<File> files = findFiles(directory, pattern);
         
         for(File file : files) {
            String path = file.getCanonicalPath();
            
            if(path != null) {
               paths.add(path);
            }
         }
      }
      return paths;
   }
   
   public List<String> findPaths(File directory, FileFilter filter) throws IOException {
      List<String> paths = new ArrayList<String>();
      
      if(directory.exists()) {
         List<File> files = findFiles(directory, filter);
         
         for(File file : files) {
            String path = file.getCanonicalPath();
            
            if(path != null) {
               paths.add(path);
            }
         }
      }
      return paths;
   }
}
