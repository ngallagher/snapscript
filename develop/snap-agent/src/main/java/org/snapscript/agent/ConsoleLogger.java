package org.snapscript.agent;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ConsoleLogger {
   
   private final PrintStream logger;
   private final DateFormat format;
   private final boolean verbose;
   
   public ConsoleLogger() {
      this(false);
   }
   
   public ConsoleLogger(boolean verbose) {
      this.format = new SimpleDateFormat("HH:mm:ss");
      this.logger = System.out;
      this.verbose = verbose;
   }
   
   public synchronized void debug(String message) {
      if(verbose) {
         log(message);
      }
   }
   
   public synchronized void debug(String message, Throwable cause) {
      if(verbose) {
         log(message, cause);
      }
   }
   
   public synchronized void log(String message) {
      long time = System.currentTimeMillis();
      Thread thread = Thread.currentThread();
      String name = thread.getName();
      String date = format.format(time);
      
      logger.println(date + " ["+name+"] " + message);
   }
   
   public synchronized void log(String message, Throwable cause) {
      long time = System.currentTimeMillis();
      Thread thread = Thread.currentThread();
      String name = thread.getName();
      String date = format.format(time);
      
      if(cause != null) {
         logger.print(date + " ["+name+"] " + message);
         
         if(verbose) {
            logger.print(": ");
            cause.printStackTrace(logger);
         } else {
            logger.println(": " + cause);
         }
      } else {
         logger.println("["+name+"] " + message);
      }
   }
}
