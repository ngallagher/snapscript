package org.snapscript.engine;

import java.io.File;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;
import org.snapscript.agent.ProcessAgent;

public class ConfigurationLoader {

   private static final String IGNORE_SNAP_CLASS_PATH = "ignore.snap.class.path";
   private static final String CONFIGURATION_FILE = "project.xml";
   private static final String JAVA_CLASS_PATH = "java.class.path";
   private static final String PATH_SEPARATOR = "path.separator";
   private static final int DEFAULT_MAX_MEMORY = 200 * 1024 * 1024;
   private static final int DEFAULT_MIN_MEMORY = 10 * 1024 * 1024;
   
   private final ConfigurationFilter filter;
   private final Persister persister;
   private final File file;
   
   public ConfigurationLoader(File file) {
      this(file, CONFIGURATION_FILE);
   }
   
   public ConfigurationLoader(File file, String name) {
      this.filter = new ConfigurationFilter();
      this.persister = new Persister(filter);
      this.file = new File(file, name);
   }
   
   public void load(ProcessConfiguration configuration) {
      String path = System.getProperty(JAVA_CLASS_PATH);
      String separator = System.getProperty(PATH_SEPARATOR);
      int maxMemory = DEFAULT_MAX_MEMORY;
      int minMemory = DEFAULT_MIN_MEMORY;
      
      try {
         if(file.exists()) {
            ConfigurationSchema specification = persister.read(ConfigurationSchema.class, file);
            List<String> paths = specification.getPaths();
            StringBuilder builder = new StringBuilder();
            String delimeter = "";

            if(!Boolean.getBoolean(IGNORE_SNAP_CLASS_PATH)) { // should we inject to class path
               File file = ClassPathScanner.findLocation(ProcessAgent.class); // where is the process agent?
               String normal = file.getCanonicalPath();
               
               System.out.println(normal);
               builder.append(delimeter);
               builder.append(normal); // append to class path
               delimeter = separator;
            }
            for(String entry : paths) {
               List<File> matches = FilePatternScanner.scan(entry);
               
               for(File match : matches) {
                  String normal = match.getCanonicalPath();
                  
                  System.out.println(normal);
                  builder.append(delimeter);
                  builder.append(normal);
                  delimeter = separator;
               }
            }
            maxMemory = specification.getMaxMemory();
            minMemory = specification.getMinMemory();
            path = builder.toString();
         }
      } catch(Exception e) {
         throw new IllegalStateException("Could not load " + file, e);
      }
      configuration.setClassPath(path);
      configuration.setMinMemory(minMemory);
      configuration.setMaxMemory(maxMemory);
   }

   @Root
   private static class ConfigurationSchema {

      @Attribute(required=false)
      private int maxMemory;
      
      @Attribute(required=false)
      private int minMemory;
      
      @ElementList(entry="path", inline=true)
      private List<String> paths;
      
      public List<String> getPaths() {
         return paths;
      }
      
      public int getMaxMemory() {
         return maxMemory;
      }
      
      public int getMinMemory(){
         return minMemory;
      }
   }
}