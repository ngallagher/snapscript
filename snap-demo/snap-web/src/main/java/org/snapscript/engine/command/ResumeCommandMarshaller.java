package org.snapscript.engine.command;

public class ResumeCommandMarshaller implements CommandMarshaller<ResumeCommand> {

   @Override
   public ResumeCommand toCommand(String value) {
      int offset = value.indexOf(':');
      String text = value.substring(offset + 1);
      return new ResumeCommand(text);
   }

   @Override
   public String fromCommand(ResumeCommand command) {
      String thread = command.getThread();
      return CommandType.RESUME + ":" + thread;
   }
}
