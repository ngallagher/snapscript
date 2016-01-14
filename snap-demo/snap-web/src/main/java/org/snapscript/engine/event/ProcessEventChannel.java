package org.snapscript.engine.event;

public interface ProcessEventChannel {
   void send(ProcessEvent event) throws Exception;
   void close() throws Exception;
}
