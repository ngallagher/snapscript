package org.snapscript.engine.event;

import java.util.HashMap;
import java.util.Map;

public class ProcessEventProducer {

   private final Map<Class, ProcessEventMarshaller> marshallers;
   private final MessageEnvelopeWriter writer;
   
   public ProcessEventProducer(MessageEnvelopeWriter writer) {
      this.marshallers = new HashMap<Class, ProcessEventMarshaller>();
      this.writer = writer;
   }
   
   public void produce(ProcessEvent object) throws Exception {
      if(marshallers.isEmpty()) {
         ProcessEventType[] events = ProcessEventType.values();
         
         for(ProcessEventType event : events) {
            ProcessEventMarshaller marshaller = event.marshaller.newInstance();
            marshallers.put(event.event, marshaller);
         }
      }
      Class type = object.getClass();
      ProcessEventMarshaller marshaller = marshallers.get(type);
      MessageEnvelope message = marshaller.toMessage(object);
      
      writer.write(message);
   }
}
