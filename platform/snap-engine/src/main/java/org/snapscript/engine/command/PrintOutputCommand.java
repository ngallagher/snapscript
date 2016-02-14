package org.snapscript.engine.command;

public class PrintOutputCommand implements Command {

   private String text;
   
   public PrintOutputCommand() {
      super();
   }
   
   public PrintOutputCommand(String text) {
      this.text = text;
   }
   
   public String getText() {
      return text;
   }
   
   public void setText(String text) {
      this.text = text;
   }
}
