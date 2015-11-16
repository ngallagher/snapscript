package org.snapscript.interpret.console;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JTextArea;

public class ConsoleWriter {

   private final Queue<Object> window;
   private final Queue<Object> queue;
   private final AtomicBoolean running;
   private final StringBuilder builder;
   private final Executor executor;
   private final JTextArea console;
   private final int capacity;

   public ConsoleWriter(Executor executor, JTextArea console) {
      this(executor, console, 1000);
   }
   
   public ConsoleWriter(Executor executor, JTextArea console, int capacity) {
      this.queue = new ConcurrentLinkedQueue<Object>();
      this.window = new ConcurrentLinkedQueue<Object>();
      this.running = new AtomicBoolean();
      this.builder = new StringBuilder();
      this.executor = executor;
      this.console = console;
      this.capacity = capacity;
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
         while(!queue.isEmpty()) {
            Object text = queue.poll();
            
            if(text != null) {
               int size = window.size();
            
               if(size > capacity) {
                  window.poll();
               }
               window.offer(text);
            }
         }
         builder.setLength(0);
         
         for(Object line : window) {
            builder.append(line);
            builder.append("\r\n");
         }
         String result = builder.toString();
         
         console.setText(result);
      }
   }
}
