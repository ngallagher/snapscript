package org.snapscript.engine.command;

public class PrintErrorCommandMarshaller implements CommandMarshaller<PrintErrorCommand>{

   @Override
   public PrintErrorCommand toCommand(String value) {
      int offset = value.indexOf(':');
      String text = value.substring(offset + 1);
      return new PrintErrorCommand(text);
   }

   @Override
   public String fromCommand(PrintErrorCommand command) {
      String text = command.getText();
      return CommandType.PRINT_ERROR + ":" + text;
   }

}
