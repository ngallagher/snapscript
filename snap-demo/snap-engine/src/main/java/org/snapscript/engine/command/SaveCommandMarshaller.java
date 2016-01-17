package org.snapscript.engine.command;

public class SaveCommandMarshaller extends ObjectCommandMarshaller<SaveCommand>{
   
   public SaveCommandMarshaller() {
      super(CommandType.SAVE);
   }
}
