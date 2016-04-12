package org.snapscript.develop;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.snapscript.develop.common.FilePatternScanner;

public class ConfigurationLoader {

   private static final String CONFIGURATION_FILE = ".project";
   private static final String JAVA_CLASS_PATH = "java.class.path";
   private static final String PATH_SEPARATOR = "path.separator";
   
   private final ConfigurationReader reader;
   
   public ConfigurationLoader(Workspace workspace) {
      this(workspace, CONFIGURATION_FILE);
   }
   
   public ConfigurationLoader(Workspace workspace, String name) {
      this.reader = new ConfigurationReader(workspace, name);
   }

   public void load(ProcessConfiguration configuration) {
      String path = System.getProperty(JAVA_CLASS_PATH);
      String separator = System.getProperty(PATH_SEPARATOR);
      Configuration data = reader.load();
      
      try {
         StringBuilder builder = new StringBuilder();
         
         if(data != null) {
            Map<String, String> environment = configuration.getVariables();
            Map<String, String> variables = data.getVariables();
            List<String> arguments = configuration.getArguments();
            List<File> dependencies = data.getDependencies();
            List<String> values = data.getArguments();
            String delimeter = "";

            if(dependencies != null) {
               for(File dependency : dependencies) {
                  if(data.isValidate() && !dependency.exists()) {
                     throw new IllegalStateException("Could not find dependency " + dependency);
                  }
                  String normal = dependency.getCanonicalPath();
                  
                  builder.append(delimeter);
                  builder.append(normal);
                  delimeter = separator;
               }
            }
            if(variables != null) {
               Set<String> names = variables.keySet();
               
               for(String name : names) {
                  String value = variables.get(name);
                  environment.put(name, value);
               }
            }
            if(values != null) {
               for(String value : values) {
                  String token = value.trim();
                  
                  if(!token.isEmpty()) {
                     arguments.add(token);
                  }
               }
            }
            if(dependencies != null) {
               path = builder.toString();
            }
         }
      } catch(Exception e) {
         throw new IllegalStateException("Could not load configuration", e);
      }
      configuration.setClassPath("." + separator + path);
   }

}
