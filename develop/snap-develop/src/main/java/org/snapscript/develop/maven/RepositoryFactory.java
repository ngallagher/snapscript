package org.snapscript.develop.maven;

import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.snapscript.agent.ConsoleLogger;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.repository.LocalRepository;
import org.sonatype.aether.repository.RemoteRepository;

public class RepositoryFactory {

   private final ConsoleLogger logger;

   public RepositoryFactory(ConsoleLogger logger) {
      this.logger = logger;
   }

   public RepositorySystem newRepositorySystem() {
      return ManualRepositorySystemFactory.newRepositorySystem();
   }

   public RepositorySystemSession newRepositorySystemSession(RepositorySystem system, String path) {
      MavenRepositorySystemSession session = new MavenRepositorySystemSession();
      LocalRepository localRepo = new LocalRepository(path);

      session.setLocalRepositoryManager(system.newLocalRepositoryManager(localRepo));
      session.setTransferListener(new ConsoleTransferListener(logger));
      session.setRepositoryListener(new ConsoleRepositoryListener(logger));

      return session;
   }

   public RemoteRepository newRemoteRepository(String name, String type, String location) {
      return new RemoteRepository(name, type, location);
   }
}
