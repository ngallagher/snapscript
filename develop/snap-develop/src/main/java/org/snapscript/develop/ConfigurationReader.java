package org.snapscript.develop;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.core.Validate;
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
            return persister.read(ProjectConfiguration.class, file);
         }
      }catch(Exception e) {
         throw new IllegalStateException("Could not read configuration", e);
      }
      return null;
   }  
   
   @Root
   private static class ProjectConfiguration implements Configuration {
      
      @Path("dependencies")
      @Attribute(name="repository", required=false)
      private String repository;
      
      @Path("dependencies")
      @Attribute(name="validate", required=false)
      private Boolean validate;
      
      @Path("dependencies")
      @ElementList(entry="dependency", required=false, inline=true)
      private List<Dependency> dependencies;
      
      @ElementList(entry="argument", required=false)
      private List<String> arguments;
      
      @ElementList(entry="variable", required=false)
      private Dictionary<EnvironmentVariable> environment;
      
      @Override
      public boolean isValidate() {
         return Boolean.TRUE.equals(validate);
      }
      
      @Override
      public Map<String, String> getVariables() {
         Map<String, String> map = new LinkedHashMap<String, String>();
         
         for(EnvironmentVariable data : environment) {
            map.put(data.name, data.value);
         }
         return map;
      }
      
      @Override
      public List<File> getDependencies() {
         List<File> files = new ArrayList<File>();
      
         try {
            if(repository == null) {
               repository = System.getProperty("user.home");
            }
            for(Dependency dependency : dependencies) {
               List<String> matches = dependency.getDependencies(repository);
               
               for(String match : matches) {
                  File file = new File(match);
                  File path = file.getCanonicalFile();
                  
                  if(!file.exists() && isValidate()) {
                     throw new IllegalStateException("Could not resolve file " + path);
                  }
                  files.add(path);
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
   
   private static class Dependency{
      
      @Attribute(required=false)
      private String pattern;
      
      @Element(required=false)
      private String groupId;
      
      @Element(required=false)
      private String artifactId;
      
      @Element(required=false)
      private String version;
      
      @Validate
      public void validate() {
         if(pattern == null) {
            if(groupId == null || version == null || artifactId == null) {
               throw new IllegalStateException("Dependency requires artifactId, groupId, and version");
            }
         } else {
            if(groupId != null || version != null || artifactId != null) {
               throw new IllegalStateException("Dependency pattern must not contain artifactId, groupId, and version");
            }
         }
      }

      public List<String> getDependencies(String repository) {
         List<String> results = new ArrayList<String>();
         
         try {
            if(pattern != null) {
               List<File> matches = FilePatternScanner.scan(pattern);
               
               for(File match : matches) {
                  String normal = match.getCanonicalPath();
                  results.add(normal);
               }
            } else {
               String root = String.format("%s/.m2/repository", repository);
               String directory = groupId.replace('.', '/');
               String path = String.format("%s/%s/%s/%s/%s-%s.jar", root, directory, artifactId, version, artifactId, version);

               results.add(path);
            }
         } catch(Exception e) {
            throw new IllegalStateException("Could not resolve '" + pattern + "'", e);
         }
         return results;
      }  
   }
   
   @Root
   private static class EnvironmentVariable implements Entry {
      
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
