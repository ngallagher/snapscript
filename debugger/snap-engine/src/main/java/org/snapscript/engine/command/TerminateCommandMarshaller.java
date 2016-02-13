package org.snapscript.engine.command;

public class TerminateCommandMarshaller implements CommandMarshaller<TerminateCommand>{

   @Override
   public TerminateCommand toCommand(String text) {
      return new TerminateCommand();
   }

   @Override
   public String fromCommand(TerminateCommand command) {
      return CommandType.TERMINATE.name();
   }
}
