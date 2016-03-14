package org.snapscript.agent;

import org.snapscript.agent.event.ProcessEventChannel;

public class ResourceExecutor {

   private final ProcessContext context;

   public ResourceExecutor(ProcessContext context) throws Exception {
      this.context = context;
   }

   public void execute(ProcessEventChannel channel, String project, String resource) throws Exception {
      ProcessTask task = new ProcessTask(channel, context, project, resource);
      Thread thread = new Thread(task);
      thread.start();
   }
}
