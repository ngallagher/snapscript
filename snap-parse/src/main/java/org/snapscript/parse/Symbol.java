package org.snapscript.parse;

public enum Symbol {
   IDENTIFIER("identifier", 0, 0x0001) {
      @Override
      public boolean read(SyntaxReader node) {
         return node.identifier();
      }
   },
   TYPE("type", 1, 0x0002) {
      @Override
      public boolean read(SyntaxReader node) {
         return node.type();
      }
   },   
   QUALIFIER("qualifier", 2, 0x0004) {
      @Override
      public boolean read(SyntaxReader node) {
         return node.qualifier();
      }
   },   
   HEXIDECIMAL("hexidecimal", 3, 0x0008) {
      @Override
      public boolean read(SyntaxReader node) {
         return node.hexidecimal();
      }
   },   
   DECIMAL("decimal", 4, 0x0010) {
      @Override
      public boolean read(SyntaxReader node) {
         return node.decimal();
      }
   },
   INTEGER("integer", 5, 0x0020) {
      @Override
      public boolean read(SyntaxReader node) {
         return node.integer();
      }
   },
   TEXT("text", 6, 0x0040) {
      @Override
      public boolean read(SyntaxReader node) {
         return node.text();
      }
   };
   
   public final String name;
   public final int index;
   public final int mask;
   
   private Symbol(String name, int index, int mask) {
      this.index = index;
      this.name = name;
      this.mask = mask;
   }

   public abstract boolean read(SyntaxReader node);
}
