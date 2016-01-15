package org.snapscript.engine.command;

public class PrintOutputCommandMarshaller implements CommandMarshaller<PrintOutputCommand> {

   @Override
   public PrintOutputCommand toCommand(String value) {
      int offset = value.indexOf(':');
      String text = value.substring(offset + 1);
      return new PrintOutputCommand(text);
   }

   @Override
   public String fromCommand(PrintOutputCommand command) {
      String text = command.getText();
      return CommandType.PRINT_OUTPUT + ":" + text;
   }

}
