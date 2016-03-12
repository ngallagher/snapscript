package org.snapscript.engine.command;

public class AlertCommandMarshaller extends ObjectCommandMarshaller<AlertCommand>{
   
   public AlertCommandMarshaller() {
      super(CommandType.ALERT);
   }
}
