package org.snapscript.agent.event;

public interface ProcessEventChannel {
   boolean send(ProcessEvent event) throws Exception;
   void close() throws Exception;
   int port() throws Exception;
}
