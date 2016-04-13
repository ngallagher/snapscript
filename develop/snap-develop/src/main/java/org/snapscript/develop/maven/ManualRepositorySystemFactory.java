package org.snapscript.develop.maven;

import org.apache.maven.repository.internal.DefaultServiceLocator;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.connector.file.FileRepositoryConnectorFactory;
import org.sonatype.aether.connector.wagon.WagonProvider;
import org.sonatype.aether.connector.wagon.WagonRepositoryConnectorFactory;
import org.sonatype.aether.spi.connector.RepositoryConnectorFactory;

public class ManualRepositorySystemFactory {

   public static RepositorySystem newRepositorySystem() {
      DefaultServiceLocator locator = new DefaultServiceLocator();
      locator.addService(RepositoryConnectorFactory.class, FileRepositoryConnectorFactory.class);
      locator.addService(RepositoryConnectorFactory.class, WagonRepositoryConnectorFactory.class);
      locator.setServices(WagonProvider.class, new ManualWagonProvider());

      return locator.getService(RepositorySystem.class);
   }

}