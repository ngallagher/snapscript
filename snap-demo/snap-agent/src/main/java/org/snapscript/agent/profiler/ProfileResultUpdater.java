package org.snapscript.agent.profiler;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.agent.event.ProcessEventChannel;
import org.snapscript.agent.event.ProfileEvent;

public class ProfileResultUpdater implements Runnable {

   private final AtomicReference<String> reference;
   private final ProcessEventChannel channel;
   private final ProcessProfiler profiler;

   public ProfileResultUpdater(ProcessProfiler profiler, ProcessEventChannel channel) {
      this.reference = new AtomicReference<String>();
      this.profiler = profiler;
      this.channel = channel;
   }
   
   public void start(String process) {
      if(reference.compareAndSet(null, process)) {
         Thread thread = new Thread(this);
         thread.start();
      }
   }

   @Override
   public void run() {
      while(true) {
         String process = reference.get();
         try {
            Thread.sleep(5000);
            Set<ProfileResult> results = profiler.lines(2000);
            ProfileEvent event = new ProfileEvent(process, results);
            channel.send(event);
         }catch(Exception e) {
            e.printStackTrace();
         }
      }
   }
   
   
}
