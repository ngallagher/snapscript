package org.snapscript.engine.http;

import static org.springframework.beans.factory.config.PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_FALLBACK;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

public class WebSite {

   private final ClassPathXmlApplicationContext appContext;
   private final Resource[] propertyFiles;

   public WebSite(Resource configFile, Resource... propertyFiles) throws IOException {
      this(configFile.getFile(), propertyFiles);
   }

   public WebSite(File configFile, Resource... propertyFiles) throws IOException {
      this(configFile.getPath(), propertyFiles);
   }

   public WebSite(String configFile, Resource... propertyFiles) throws IOException {
      this.appContext = new ClassPathXmlApplicationContext(new String[] { configFile }, false);
      this.propertyFiles = propertyFiles;
   }

   public void start() throws Exception {
      PropertyPlaceholderConfigurer propertyConfigurer = new PropertyPlaceholderConfigurer();
      propertyConfigurer.setLocations(propertyFiles);
      propertyConfigurer.setSystemPropertiesMode(SYSTEM_PROPERTIES_MODE_FALLBACK);
      appContext.addBeanFactoryPostProcessor(propertyConfigurer);
      appContext.refresh();
      appContext.registerShutdownHook();
   }

   public static void main(String[] list) throws Exception {
      WebSite service = new WebSite("/context/application.xml");
      service.start();
   }

}
