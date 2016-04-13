package org.snapscript.develop.maven;

import org.snapscript.agent.ConsoleLogger;
import org.sonatype.aether.AbstractRepositoryListener;
import org.sonatype.aether.RepositoryEvent;

public class ConsoleRepositoryListener extends AbstractRepositoryListener {

   private ConsoleLogger logger;

   public ConsoleRepositoryListener(ConsoleLogger logger) {
      this.logger = logger;
   }

   public void artifactDeployed(RepositoryEvent event) {
      logger.log("Deployed " + event.getArtifact() + " to " + event.getRepository());
   }

   public void artifactDeploying(RepositoryEvent event) {
      logger.log("Deploying " + event.getArtifact() + " to " + event.getRepository());
   }

   public void artifactDescriptorInvalid(RepositoryEvent event) {
      logger.log("Invalid artifact descriptor for " + event.getArtifact() + ": " + event.getException().getMessage());
   }

   public void artifactDescriptorMissing(RepositoryEvent event) {
      logger.log("Missing artifact descriptor for " + event.getArtifact());
   }

   public void artifactInstalled(RepositoryEvent event) {
      logger.log("Installed " + event.getArtifact() + " to " + event.getFile());
   }

   public void artifactInstalling(RepositoryEvent event) {
      logger.log("Installing " + event.getArtifact() + " to " + event.getFile());
   }

   public void artifactResolved(RepositoryEvent event) {
      logger.log("Resolved artifact " + event.getArtifact() + " from " + event.getRepository());
   }

   public void artifactDownloading(RepositoryEvent event) {
      logger.log("Downloading artifact " + event.getArtifact() + " from " + event.getRepository());
   }

   public void artifactDownloaded(RepositoryEvent event) {
      logger.log("Downloaded artifact " + event.getArtifact() + " from " + event.getRepository());
   }

   public void artifactResolving(RepositoryEvent event) {
      logger.log("Resolving artifact " + event.getArtifact());
   }

   public void metadataDeployed(RepositoryEvent event) {
      logger.log("Deployed " + event.getMetadata() + " to " + event.getRepository());
   }

   public void metadataDeploying(RepositoryEvent event) {
      logger.log("Deploying " + event.getMetadata() + " to " + event.getRepository());
   }

   public void metadataInstalled(RepositoryEvent event) {
      logger.log("Installed " + event.getMetadata() + " to " + event.getFile());
   }

   public void metadataInstalling(RepositoryEvent event) {
      logger.log("Installing " + event.getMetadata() + " to " + event.getFile());
   }

   public void metadataInvalid(RepositoryEvent event) {
      logger.log("Invalid metadata " + event.getMetadata());
   }

   public void metadataResolved(RepositoryEvent event) {
      logger.log("Resolved metadata " + event.getMetadata() + " from " + event.getRepository());
   }

   public void metadataResolving(RepositoryEvent event) {
      logger.log("Resolving metadata " + event.getMetadata() + " from " + event.getRepository());
   }

}
