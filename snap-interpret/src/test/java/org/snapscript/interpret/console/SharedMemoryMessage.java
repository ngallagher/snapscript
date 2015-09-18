package org.snapscript.interpret.console;

public class SharedMemoryMessage {

   private final int product;
   private final int bid;
   private final int offer;
   
   public SharedMemoryMessage(int product, int bid, int offer) {
      this.product = product;
      this.bid = bid;
      this.offer = offer;
   }
   
   public int getProduct(){
      return product;
   }
   
   public int getBid(){
      return bid;
   }
   
   public int getOffer() {
      return offer;
   }
}
