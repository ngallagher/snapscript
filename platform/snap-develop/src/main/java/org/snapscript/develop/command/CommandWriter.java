package org.snapscript.develop.command;

import java.util.HashMap;
import java.util.Map;

public class CommandWriter {

   private final Map<Class, CommandMarshaller> marshallers;
   
   public CommandWriter() {
      this.marshallers = new HashMap<Class, CommandMarshaller>();
   }
   
   public String write(Command object) throws Exception {
      if(marshallers.isEmpty()) {
         CommandType[] commands = CommandType.values();
         
         for(CommandType command : commands) {
            CommandMarshaller marshaller = command.marshaller.newInstance();
            marshallers.put(command.command, marshaller);
         }
      }
      Class type = object.getClass();
      CommandMarshaller marshaller = marshallers.get(type);
      
      return marshaller.fromCommand(object);
   }
}
