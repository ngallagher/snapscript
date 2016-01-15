package org.snapscript.engine.command;

public class ExitCommandMarshaller implements CommandMarshaller<ExitCommand>{

   @Override
   public ExitCommand toCommand(String text) {
      return new ExitCommand();
   }

   @Override
   public String fromCommand(ExitCommand command) {
      return CommandType.EXIT.name();
   }
}
