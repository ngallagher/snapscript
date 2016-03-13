package org.snapscript.engine;

import java.io.File;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;
import org.snapscript.agent.ProcessAgentConfiguration;

public class ProcessEngineLoader {

   private static final int DEFAULT_MAX_MEMORY = 200 * 1024 * 1024;
   private static final int DEFAULT_MIN_MEMORY = 10 * 1024 * 1024;
   private static final String CONFIGURATION_FILE = "project.xml";
   
   private final ProcessEngineFilter filter;
   private final Persister persister;
   private final File file;
   
   public ProcessEngineLoader(File file) {
      this(file, CONFIGURATION_FILE);
   }
   
   public ProcessEngineLoader(File file, String name) {
      this.filter = new ProcessEngineFilter();
      this.persister = new Persister(filter);
      this.file = new File(file, name);
   }
   
   public void load(ProcessAgentConfiguration configuration) {
      String path = System.getProperty("java.class.path");
      String separator = System.getProperty("path.separator");
      int maxMemory = DEFAULT_MAX_MEMORY;
      int minMemory = DEFAULT_MIN_MEMORY;
      
      try {
         if(file.exists()) {
            ProcessAgentSpecification specification = persister.read(ProcessAgentSpecification.class, file);
            List<String> paths = specification.getPaths();
            StringBuilder builder = new StringBuilder();
            String delimeter = "";
            
            for(String entry : paths) {
               builder.append(delimeter);
               builder.append(entry);
               delimeter = separator;
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
   private static class ProcessAgentSpecification {

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