package org.snapscript.interpret.console;

import java.nio.MappedByteBuffer;

public class SharedMemoryEncoder {

   public void encode(MappedByteBuffer buffer, SharedMemoryMessage message) {
      int product = message.getProduct();
      int bid = message.getBid();
      int offer = message.getOffer();
      
      buffer.putInt(product);
      buffer.putInt(bid);
      buffer.putInt(offer);
   }
   
   public SharedMemoryMessage decode(MappedByteBuffer buffer) {
      int product = buffer.getInt();
      int bid = buffer.getInt();
      int offer = buffer.getInt();
      
      return new SharedMemoryMessage(product, bid, offer);
   }
}
