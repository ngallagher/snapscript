package org.snapscript.engine.command;

public class StatusCommandMarshaller extends ObjectCommandMarshaller<StatusCommand>{
   
   public StatusCommandMarshaller() {
      super(CommandType.STATUS);
   }
}
