package org.snapscript.engine.command;

public enum CommandType {
   PRINT_OUTPUT(PrintOutputCommandMarshaller.class, PrintOutputCommand.class),
   PRINT_ERROR(PrintErrorCommandMarshaller.class, PrintErrorCommand.class),
   EXECUTE(ExecuteCommandMarshaller.class, ExecuteCommand.class),
   SAVE(SaveCommandMarshaller.class, SaveCommand.class),
   DELETE(DeleteCommandMarshaller.class, DeleteCommand.class),
   RELOAD_TREE(ReloadTreeCommandMarshaller.class, ReloadTreeCommand.class),
   SUSPEND(SuspendCommandMarshaller.class, SuspendCommand.class),
   TERMINATE(TerminateCommandMarshaller.class, TerminateCommand.class),
   EXIT(ExitCommandMarshaller.class, ExitCommand.class),
   PROBLEM(ProblemCommandMarshaller.class, ProblemCommand.class),
   SCOPE(ScopeCommandMarshaller.class, ScopeCommand.class),
   RESUME(ResumeCommandMarshaller.class, ResumeCommand.class);
   
   public final Class<? extends CommandMarshaller> marshaller;
   public final Class<? extends Command> command;
   
   private CommandType(Class<? extends CommandMarshaller> marshaller, Class<? extends Command> command) {
      this.marshaller = marshaller;
      this.command = command;
   }
}
