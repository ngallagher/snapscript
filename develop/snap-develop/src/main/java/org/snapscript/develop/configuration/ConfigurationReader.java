package org.snapscript.develop.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.util.Dictionary;
import org.simpleframework.xml.util.Entry;
import org.snapscript.agent.ConsoleLogger;
import org.snapscript.develop.Workspace;
import org.snapscript.develop.maven.RepositoryClient;
import org.snapscript.develop.maven.RepositoryFactory;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.repository.RemoteRepository;

public class ConfigurationReader {

   private static final String CONFIGURATION_FILE = ".project";
   
   private final AtomicReference<Configuration> reference;
   private final ConfigurationFilter filter;
   private final RepositoryFactory factory;
   private final Persister persister;
   private final Workspace workspace;
   private final String name;
   
   public ConfigurationReader(ConsoleLogger logger, Workspace workspace) {
      this(logger, workspace, CONFIGURATION_FILE);
   }
   
   public ConfigurationReader(ConsoleLogger logger, Workspace workspace, String name) {
      this.reference = new AtomicReference<Configuration>();
      this.factory = new RepositoryFactory(logger);
      this.filter = new ConfigurationFilter();
      this.persister = new Persister(filter);
      this.workspace = workspace;
      this.name = name;
   }

   public Configuration load() {
      Configuration configuration = reference.get();
      
      if(configuration == null) {
         try {
            File file = workspace.create(name);
            
            if(file.exists()) {
               ProjectDetails details = persister.read(ProjectDetails.class, file);
               configuration = new ConfigurationDetails(details, factory);
               reference.set(configuration);
            }
         }catch(Exception e) {
            throw new IllegalStateException("Could not read configuration", e);
         }
      }
      return configuration;
   }  
   
   private static class ConfigurationDetails implements Configuration {
      
      private final RepositoryFactory factory;
      private final ProjectDetails details;
      
      public ConfigurationDetails(ProjectDetails details, RepositoryFactory factory){
         this.details = details;
         this.factory = factory;
      }

      @Override
      public Map<String, String> getVariables() {
         return details.getVariables();
      }

      @Override
      public List<File> getDependencies() {
         return details.getDependencies(factory);
      }

      @Override
      public List<String> getArguments() {
         return details.getArguments();
      }
   }
   
   @Root
   private static class ProjectDetails {
      
      @Element(name="repository", required=false)
      private RepositoryConfiguration repository;
      
      @Path("dependencies")
      @ElementList(entry="dependency", required=false, inline=true)
      private List<Dependency> dependencies;
      
      @ElementList(entry="argument", required=false)
      private List<String> arguments;
      
      @ElementList(entry="variable", required=false)
      private Dictionary<EnvironmentVariable> environment;
      
      public void validate() {
         if(dependencies != null) {
            if(repository == null) {
               throw new IllegalStateException("No repository has been defined");
            }
         }
      }

      public Map<String, String> getVariables() {
         Map<String, String> map = new LinkedHashMap<String, String>();
         
         if(environment != null) {
            for(EnvironmentVariable data : environment) {
               map.put(data.name, data.value);
            }
         }
         return map;
      }
      
      public List<File> getDependencies(RepositoryFactory factory) {
         List<File> files = new ArrayList<File>();
      
         try {
            RepositoryClient client = repository.getClient(factory);
            
            for(Dependency dependency : dependencies) {
               List<File> matches = dependency.getDependencies(client);
               
               for(File match : matches) {
                  if(!match.exists()) {
                     throw new IllegalStateException("Could not resolve file " + match);
                  }
                  files.add(match);
               }
            }
         } catch(Exception e) {
            throw new IllegalStateException("Could not resolve dependencies", e);
         }
         return files;
      }
      
      public List<String> getArguments() {
         if(arguments != null) {
            return arguments;
         }
         return Collections.emptyList();
      }
   }
   
   @Root
   private static class Dependency{

      @Element
      private String groupId;
      
      @Element
      private String artifactId;
      
      @Element
      private String version;

      public List<File> getDependencies(RepositoryClient client) {
         try {
            return client.resolve(groupId, artifactId, version);
         } catch(Exception e) {
            throw new IllegalStateException("Could not resolve '" + groupId + ":" + artifactId + ":" +version, e);
         }
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
   
   @Root
   private static class RepositoryConfiguration {
      
      @Attribute
      private String path;
      
      @ElementList(entry="location", inline=true)
      private List<RepositoryLocation> repositories;
      
      public RepositoryClient getClient(RepositoryFactory factory) {
         List<RemoteRepository> list = new ArrayList<RemoteRepository>();
         
         for(RepositoryLocation repository : repositories) {
            RemoteRepository remote = repository.getRepository(factory);
            list.add(remote);
         }
         RepositorySystem system = factory.newRepositorySystem();
         return new RepositoryClient(list, system, factory, path);
      }
   }
   
   @Root
   private static class RepositoryLocation implements Entry {
      
      @Text
      private String location;
      
      @Attribute
      private String name;

      public RemoteRepository getRepository(RepositoryFactory factory) {
         return factory.newRemoteRepository(name, "default", location);
      }

      @Override
      public String getName() {
         return name;
      }
   }
}
