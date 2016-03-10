package org.snapscript.parse;


public enum Symbol {
   IDENTIFIER("identifier") {
      @Override
      public boolean read(SyntaxReader node) {
         return node.identifier();
      }
   },
   TYPE("type") {
      @Override
      public boolean read(SyntaxReader node) {
         return node.type();
      }
   },   
   QUALIFIER("qualifier") {
      @Override
      public boolean read(SyntaxReader node) {
         return node.qualifier();
      }
   },   
   HEXIDECIMAL("hexidecimal") {
      @Override
      public boolean read(SyntaxReader node) {
         return node.hexidecimal();
      }
   },   
   BINARY("binary") {
      @Override
      public boolean read(SyntaxReader node) {
         return node.binary();
      }
   },
   DECIMAL("decimal") {
      @Override
      public boolean read(SyntaxReader node) {
         return node.decimal();
      }
   },
   TEXT("text") {
      @Override
      public boolean read(SyntaxReader node) {
         return node.text();
      }
   },
   TEMPLATE("template") {
      @Override
      public boolean read(SyntaxReader node) {
         return node.template();
      }
   };
   
   public final String name;
   
   private Symbol(String name) {
      this.name = name;
   }

   public abstract boolean read(SyntaxReader node);
}
