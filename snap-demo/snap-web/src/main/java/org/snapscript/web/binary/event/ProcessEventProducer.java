package org.snapscript.web.binary.event;

import java.util.HashMap;
import java.util.Map;

import org.snapscript.web.binary.BinaryMessage;
import org.snapscript.web.binary.BinaryMessageProducer;

public class ProcessEventProducer {

   private final Map<Class, ProcessEventMarshaller> marshallers;
   private final BinaryMessageProducer producer;
   
   public ProcessEventProducer(BinaryMessageProducer producer) {
      this.marshallers = new HashMap<Class, ProcessEventMarshaller>();
      this.producer = producer;
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
      BinaryMessage message = marshaller.toMessage(object);
      
      producer.produce(message);
   }
}
