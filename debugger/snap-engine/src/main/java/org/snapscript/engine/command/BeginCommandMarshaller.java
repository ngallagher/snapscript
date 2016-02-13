package org.snapscript.engine.command;

public class BeginCommandMarshaller extends ObjectCommandMarshaller<BeginCommand>{
   
   public BeginCommandMarshaller() {
      super(CommandType.BEGIN);
   }
}