package org.snapscript.engine.event;

public enum ProcessEventType {
   WRITE_OUTPUT(WriteOutputEventMarshaller.class, WriteOutputEvent.class, 1),
   WRITE_ERROR(WriteErrorEventMarshaller.class, WriteErrorEvent.class, 2),
   PING(PingEventMarshaller.class, PingEvent.class, 3),
   PONG(PongEventMarshaller.class, PongEvent.class, 4),
   EXECUTE(ExecuteEventMarshaller.class, ExecuteEvent.class, 5),
   REGISTER(RegisterEventMarshaller.class, RegisterEvent.class, 6),
   SYNTAX_ERROR(SyntaxErrorEventMarshaller.class, SyntaxErrorEvent.class, 7),
   EXIT(ExitEventMarshaller.class, ExitEvent.class, 8);
   
   public final Class<? extends ProcessEventMarshaller> marshaller;
   public final Class<? extends ProcessEvent> event;
   public final int code;
   
   private ProcessEventType(Class<? extends ProcessEventMarshaller> marshaller, Class<? extends ProcessEvent> event, int code) {
      this.marshaller = marshaller;
      this.event = event;
      this.code = code;
   }
}
