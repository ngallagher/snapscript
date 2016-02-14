package org.snapscript.parse;

public class SyntaxPosition {
   
   private final char[] source;
   private final int hash;
   private final int count;
   private final int off;
   
   public SyntaxPosition(char[] source, int off, int count, int hash) {
      this.source = source;
      this.count = count;
      this.hash = hash;
      this.off = off;
   }
   
   @Override
   public boolean equals(Object object) {
      if(this != object) {
         return equals((SyntaxPosition)object);
      }
      return true;
   }
   
   public boolean equals(SyntaxPosition object) {
      if(object.off != off) {
         return false;
      }
      if(object.count != count) {
         return false;
      }
      if(object.hash != hash) {
         return false;
      }
      if(object.source != source) { 
         for(int i = off; i < count; i++) {
            if(object.source[off] != source[off]) {
               return false;
            }
         }
      }
      return true;   
   }
   
   @Override
   public int hashCode() {
      return hash ^ off;
   }
   
   @Override
   public String toString() {
      return new String(source, off, count - off);
   }
}
