package org.snapscript.web;

import java.io.PrintStream;

public class ConsoleWriter {

   private final PrintStream console;
   private final String color;

   public ConsoleWriter(PrintStream console, String color) {
      this.console = console;
      this.color = color;
   }

   public void log(Object text) {
      console.println("<p style='color: "+color+"'>"+text+"</p>");
      console.flush();
   }
}
