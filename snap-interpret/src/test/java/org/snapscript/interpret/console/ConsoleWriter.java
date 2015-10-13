package org.snapscript.interpret.console;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JTextArea;

public class ConsoleWriter {

   private final Queue<Object> queue;
   private final AtomicBoolean running;
   private final Executor executor;
   private final JTextArea console;

   public ConsoleWriter(Executor executor, JTextArea console) {
      this.queue = new ConcurrentLinkedQueue<Object>();
      this.running = new AtomicBoolean();
      this.executor = executor;
      this.console = console;
   }

   public void log(Object text) {
      if(text != null) {
         queue.offer(text);
         
         if(running.compareAndSet(false, true)) {
            drain();
         }
      }
   }
   
   private void drain() {
      executor.execute(new Runnable() { // This is rubbish!!!
         public void run() {
            log();
            
            if(running.compareAndSet(true, false)) {
               log(); // make sure its drained
            }
         }
      });
   }
   
   private synchronized void log(){
      if(!queue.isEmpty()) {
         Object text = queue.poll();
         
         if(text != null) {
            String original = console.getText();
            StringBuilder builder = new StringBuilder(original == null ? "" : original);
            builder.append(text);
            builder.append("\r\n");
            
            while(!queue.isEmpty()) {
               text = queue.poll();
               builder.append(text);
               builder.append("\r\n");
            }
            console.setText(builder.toString());
         }
      }
   }
}
