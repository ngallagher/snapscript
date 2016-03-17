package org.snapscript.develop;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.util.Dictionary;
import org.simpleframework.xml.util.Entry;

public class ConfigurationLoader {

   private static final String CONFIGURATION_FILE = ".project";
   private static final String JAVA_CLASS_PATH = "java.class.path";
   private static final String PATH_SEPARATOR = "path.separator";
   
   private final ConfigurationFilter filter;
   private final Persister persister;
   private final Workspace workspace;
   private final String name;
   
   public ConfigurationLoader(Workspace workspace) {
      this(workspace, CONFIGURATION_FILE);
   }
   
   public ConfigurationLoader(Workspace workspace, String name) {
      this.filter = new ConfigurationFilter();
      this.persister = new Persister(filter);
      this.workspace = workspace;
      this.name = name;
   }
   
   public void load(ProcessConfiguration configuration) {
      String path = System.getProperty(JAVA_CLASS_PATH);
      String separator = System.getProperty(PATH_SEPARATOR);
      File file = workspace.create(name);
      
      try {
         StringBuilder builder = new StringBuilder();
         
         if(file.exists()) {
            ConfigurationData data = persister.read(ConfigurationData.class, file);
            Map<String, String> environment = configuration.getVariables();
            Set<ConfigurationVariable> variables = data.getVariables();
            List<String> arguments = configuration.getArguments();
            List<String> dependencies = data.getDependencies();
            List<String> values = data.getArguments();
            String delimeter = "";

            if(dependencies != null) {
               for(String dependency : dependencies) {
                  List<File> matches = FilePatternScanner.scan(dependency);
                  
                  for(File match : matches) {
                     String normal = match.getCanonicalPath();
                     
                     builder.append(delimeter);
                     builder.append(normal);
                     delimeter = separator;
                  }
               }
            }
            if(variables != null) {
               for(ConfigurationVariable variable : variables) {
                  environment.put(variable.name, variable.value);
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
         throw new IllegalStateException("Could not load " + file, e);
      }
      configuration.setClassPath("." + separator + path);
   }

   @Root
   private static class ConfigurationData {
      
      @ElementList(entry="dependency", required=false)
      private List<String> dependencies;
      
      @ElementList(entry="argument", required=false)
      private List<String> arguments;
      
      @ElementList(entry="variable", required=false)
      private Dictionary<ConfigurationVariable> environment;
      
      public Set<ConfigurationVariable> getVariables() {
         return environment;
      }
      
      public List<String> getDependencies() {
         return dependencies;
      }
      
      public List<String> getArguments() {
         return arguments;
      }
   }
   
   @Root
   private static class ConfigurationVariable implements Entry {
      
      @Attribute
      private String name;
      
      @Text
      private String value;
      
      @Commit
      public void update(Map session) {
         session.put(name, value);
      }
      
      @Override
      public String getName() {
         return name;
      }
   }
}