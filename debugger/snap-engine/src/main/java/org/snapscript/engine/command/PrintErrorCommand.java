package org.snapscript.engine.command;

public class PrintErrorCommand implements Command {

   private String text;
   
   public PrintErrorCommand() {
      super();
   }
   
   public PrintErrorCommand(String text) {
      this.text = text;
   }
   
   public String getText() {
      return text;
   }
   
   public void setText(String text) {
      this.text = text;
   }
}
