package org.snapscript.engine.event;

public interface ProcessEventListener {
   void onExit(ProcessEventChannel channel, ExitEvent event) throws Exception;
   void onExecute(ProcessEventChannel channel, ExecuteEvent event) throws Exception;
   void onWriteError(ProcessEventChannel channel, WriteErrorEvent event) throws Exception;
   void onWriteOutput(ProcessEventChannel channel, WriteOutputEvent event) throws Exception;
   void onRegister(ProcessEventChannel channel, RegisterEvent event) throws Exception;
   void onSyntaxError(ProcessEventChannel channel, SyntaxErrorEvent event) throws Exception;
   void onScope(ProcessEventChannel channel, ScopeEvent event) throws Exception;
   void onSuspend(ProcessEventChannel channel, SuspendEvent event) throws Exception;
   void onStart(ProcessEventChannel channel, StartEvent event) throws Exception;
   void onResume(ProcessEventChannel channel, ResumeEvent event) throws Exception;
   void onPing(ProcessEventChannel channel, PingEvent event) throws Exception;
   void onPong(ProcessEventChannel channel, PongEvent event) throws Exception;
   void onClose(ProcessEventChannel channel) throws Exception;
}
