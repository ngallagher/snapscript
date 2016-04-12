package org.snapscript.develop;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.util.Dictionary;
import org.simpleframework.xml.util.Entry;
import org.snapscript.develop.common.FilePatternScanner;

public class ConfigurationReader {

   private static final String CONFIGURATION_FILE = ".project";
   
   private final ConfigurationFilter filter;
   private final Persister persister;
   private final Workspace workspace;
   private final String name;
   
   public ConfigurationReader(Workspace workspace) {
      this(workspace, CONFIGURATION_FILE);
   }
   
   public ConfigurationReader(Workspace workspace, String name) {
      this.filter = new ConfigurationFilter();
      this.persister = new Persister(filter);
      this.workspace = workspace;
      this.name = name;
   }

   public Configuration load() {
      File file = workspace.create(name);
      
      try {
         if(file.exists()) {
            return persister.read(ConfigurationData.class, file);
         }
      }catch(Exception e) {
         throw new IllegalStateException("Could not read configuration", e);
      }
      return null;
   }  
   
   @Root
   private static class ConfigurationData implements Configuration {
      
      @Path("dependencies")
      @Attribute(name="validate", required=false)
      private Boolean validate;
      
      @Path("dependencies")
      @ElementList(entry="dependency", required=false, inline=true)
      private List<String> dependencies;
      
      @ElementList(entry="argument", required=false)
      private List<String> arguments;
      
      @ElementList(entry="variable", required=false)
      private Dictionary<VariableData> environment;
      
      @Override
      public boolean isValidate() {
         return Boolean.TRUE.equals(validate);
      }
      
      @Override
      public Map<String, String> getVariables() {
         Map<String, String> map = new LinkedHashMap<String, String>();
         
         for(VariableData data : environment) {
            map.put(data.name, data.value);
         }
         return map;
      }
      
      @Override
      public List<File> getDependencies() {
         List<File> files = new ArrayList<File>();
      
         try {
            for(String dependency : dependencies) {
               List<File> matches = FilePatternScanner.scan(dependency);
   
               for(File match : matches) {
                  File normal = match.getCanonicalFile();
                  files.add(normal);
               }
            }
         } catch(Exception e) {
            throw new IllegalStateException("Could not resolve dependencies", e);
         }
         return files;
      }
      
      @Override
      public List<String> getArguments() {
         return arguments;
      }
   }
   
   @Root
   private static class VariableData implements Entry {
      
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
