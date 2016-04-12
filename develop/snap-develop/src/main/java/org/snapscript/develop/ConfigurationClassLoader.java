package org.snapscript.develop;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigurationClassLoader {

   private static final String CONFIGURATION_FILE = ".project";
   
   private final AtomicReference<ClassLoader> reference;
   private final ConfigurationReader reader;
   
   public ConfigurationClassLoader(Workspace workspace) {
      this(workspace, CONFIGURATION_FILE);
   }
   
   public ConfigurationClassLoader(Workspace workspace, String name) {
      this.reader = new ConfigurationReader(workspace, name);
      this.reference = new AtomicReference<ClassLoader>();
   }
   
   public Class loadClass(String name) {
      ClassLoader loader = getClassLoader();
      
      try {
         return loader.loadClass(name);
      } catch(Exception e) {
         throw new IllegalArgumentException("Could not find class " + name, e);
      }
   }
   
   public ClassLoader getClassLoader() {
      ClassLoader loader = reference.get();
      
      if(loader == null) {
         loader = getConfigurationClassLoader();
         reference.set(loader);
      }
      return loader;
   }

   private ClassLoader getConfigurationClassLoader() {
      Configuration data = reader.load();
      
      try {
         if(data != null) {
            List<File> dependencies = data.getDependencies();
            List<URL> locations = new ArrayList<URL>();
            URL[] array = new URL[]{};
            
            for(File dependency : dependencies) {
               URL location = dependency.toURI().toURL();
               locations.add(location);
            }
            return new URLClassLoader(
                  locations.toArray(array),
                  ConfigurationClassLoader.class.getClassLoader());
         }
      } catch(Exception e) {
         throw new IllegalStateException("Could not load configuration", e);
      }
      return ConfigurationClassLoader.class.getClassLoader();
   }

}
