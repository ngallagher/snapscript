package org.snapscript.web.binary.event;

import java.util.HashMap;
import java.util.Map;

import org.snapscript.web.binary.BinaryMessage;
import org.snapscript.web.binary.BinaryMessageConsumer;

public class ProcessEventConsumer {
   
   private final Map<Integer, ProcessEventMarshaller> marshallers;
   private final BinaryMessageConsumer consumer;

   public ProcessEventConsumer(BinaryMessageConsumer consumer) {
      this.marshallers = new HashMap<Integer, ProcessEventMarshaller>();
      this.consumer = consumer;
   }
   
   public ProcessEvent consume() throws Exception {
      if(marshallers.isEmpty()) {
         ProcessEventType[] events = ProcessEventType.values();
         
         for(ProcessEventType event : events) {
            ProcessEventMarshaller marshaller = event.marshaller.newInstance();
            marshallers.put(event.code, marshaller);
         }
      }
      BinaryMessage message = consumer.consume();
      int code = message.getCode();
      ProcessEventMarshaller marshaller = marshallers.get(code);
      
      return (ProcessEvent)marshaller.fromMessage(message);
   }

}
