package org.snapscript.agent.event;

public class StartEvent implements ProcessEvent {

   private String resource;
   private String process;
   private String project;
   
   public StartEvent(String process, String project, String resource) {
      this.process = process;
      this.project = project;
      this.resource = resource;
   }

   public String getResource() {
      return resource;
   }

   public void setResource(String resource) {
      this.resource = resource;
   }

   public String getProcess() {
      return process;
   }

   public void setProcess(String process) {
      this.process = process;
   }

   public String getProject() {
      return project;
   }

   public void setProject(String project) {
      this.project = project;
   }
}
