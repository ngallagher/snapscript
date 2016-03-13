package org.snapscript.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProcessEngineScanner {
   
   private static final String RECURSIVE_PATTERN = "_RECURSIVE_";
   private static final String SINGLE_PATTERN = "_SINGLE_";

   public static List<File> scan(String pattern) throws Exception {
      List<File> list = new ArrayList<File>();
      File file = new File(pattern);
      
      if(pattern.contains("*")) {
         int index = pattern.indexOf("*");
         String expression = pattern;
         
         if(index != -1) {
            String parent = pattern.substring(0, index);
            File directory = new File(parent);
            
            if(directory.exists()) {
               expression = expression.replace("**", RECURSIVE_PATTERN); // convert \** to \.*
               expression = expression.replace("*", SINGLE_PATTERN); // convert \* to file regex
               
               File path = new File(expression);
               
               try {
                  expression = path.getCanonicalPath(); // remove ../ and ./
                  
                  while(expression.contains("\\\\")) { // ensure \\ does not exist
                     expression = expression.replace("\\\\", "\\");
                  }
                  while(expression.contains("//")) { // ensure // does not exist
                     expression = expression.replace("//", "/");
                  }
                  expression = expression.replace("\\", "\\\\"); // escape \
                  expression = expression.replace(".", "\\."); // escape .
                  expression = expression.replace("(", "\\("); // escape (
                  expression = expression.replace(")", "\\)"); // escape )
                  expression = expression.replace("-", "\\-"); // escape -
                  expression = expression.replace(RECURSIVE_PATTERN, ".*");
                  expression = expression.replace(SINGLE_PATTERN, "[a-zA-Z0-9_\\-\\(\\)\\.\\s]+");
                  scan(expression, directory, list);
                  
                  if(list.isEmpty()) {
                     throw new IllegalArgumentException("Could not match file '" + pattern + "'");
                  }
                  return list;
               }catch(Exception e) {
                  throw new IllegalArgumentException("Could not parse pattern '" +pattern+ "'", e);
               }
            }
         }
      }
      if(!file.exists()) {
         throw new IllegalArgumentException("Could not match file '" + pattern + "'");
      }
      return Collections.singletonList(file);
   }
   
   private static List<File> scan(String pattern, File directory, List<File> files) throws Exception {
      if(directory.exists()) {
         File[] list = directory.listFiles();
         String normal = directory.getCanonicalPath();
         
         if(normal.matches(pattern)) {
            files.add(directory);
         } else {
            for(File entry : list) {
               normal = entry.getCanonicalPath();
               
               if(normal.matches(pattern)) {
                  if(entry.isFile()) {
                     files.add(entry);
                  }
               }
               if(entry.isDirectory()) {
                  scan(pattern, entry, files);
               }
            }
         }
      }
      return files;
   }
}
