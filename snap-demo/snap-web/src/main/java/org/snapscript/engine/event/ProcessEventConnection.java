package org.snapscript.engine.event;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.snapscript.engine.message.BinaryMessageConsumer;
import org.snapscript.engine.message.BinaryMessageProducer;

public class ProcessEventConnection {

   private final BinaryMessageConsumer consumer;
   private final BinaryMessageProducer producer;
   
   public ProcessEventConnection(InputStream input, OutputStream output) {
      this.consumer = new BinaryMessageConsumer(input);
      this.producer = new BinaryMessageProducer(output);
   }
   
   public ProcessEventConsumer getConsumer() throws IOException {
      return new ProcessEventConsumer(consumer);
   }
   
   public ProcessEventProducer getProducer() throws IOException {
      return new ProcessEventProducer(producer);
      
   }
}
