package org.snapscript.engine.command;

import static org.snapscript.engine.command.CommandOrigin.CLIENT;
import static org.snapscript.engine.command.CommandOrigin.ENGINE;
import static org.snapscript.engine.command.CommandOrigin.PROCESS;

public enum CommandType {
   PRINT_OUTPUT(PrintOutputCommandMarshaller.class, PrintOutputCommand.class, PROCESS),
   PRINT_ERROR(PrintErrorCommandMarshaller.class, PrintErrorCommand.class, PROCESS),
   EXECUTE(ExecuteCommandMarshaller.class, ExecuteCommand.class, CLIENT),
   SAVE(SaveCommandMarshaller.class, SaveCommand.class, CommandOrigin.CLIENT),
   DELETE(DeleteCommandMarshaller.class, DeleteCommand.class, CommandOrigin.CLIENT),
   RELOAD_TREE(ReloadTreeCommandMarshaller.class, ReloadTreeCommand.class, ENGINE),
   SUSPEND(SuspendCommandMarshaller.class, SuspendCommand.class, CommandOrigin.CLIENT),
   TERMINATE(TerminateCommandMarshaller.class, TerminateCommand.class, ENGINE),
   EXIT(ExitCommandMarshaller.class, ExitCommand.class, PROCESS),
   STOP(StopCommandMarshaller.class, StopCommand.class, CLIENT),
   PROBLEM(ProblemCommandMarshaller.class, ProblemCommand.class, ENGINE),
   SCOPE(ScopeCommandMarshaller.class, ScopeCommand.class, PROCESS),
   RESUME(ResumeCommandMarshaller.class, ResumeCommand.class, CLIENT);
   
   public final Class<? extends CommandMarshaller> marshaller;
   public final Class<? extends Command> command;
   public final CommandOrigin origin;
   
   private CommandType(Class<? extends CommandMarshaller> marshaller, Class<? extends Command> command, CommandOrigin origin) {
      this.marshaller = marshaller;
      this.command = command;
      this.origin = origin;
   }
}
