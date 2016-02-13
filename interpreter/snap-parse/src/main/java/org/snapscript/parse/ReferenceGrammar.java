package org.snapscript.parse;

import java.util.concurrent.atomic.AtomicReference;

public class ReferenceGrammar implements Grammar {

   private final AtomicReference<Grammar> cache;
   private final GrammarResolver resolver; 
   private final String name;
   private final int index;
   
   public ReferenceGrammar(GrammarResolver resolver, String name, int index) {
      this.cache = new AtomicReference<Grammar>();
      this.resolver = resolver;
      this.index = index;
      this.name = name;
   }   
   
   @Override
   public boolean read(SyntaxReader node, int depth) {
      Grammar grammar = cache.get();
      
      if(grammar == null) {
         grammar = resolver.resolve(name);
         
         if(grammar == null) {
            throw new IllegalArgumentException("Grammar '" + name + "' does not exist");
         }
         cache.set(grammar);
      }      
      SyntaxReader child = node.mark(index);   

      if(child != null) {
         if(grammar.read(child, 0)) {
            child.commit();
            return true;
         }
         child.reset();
      }
      return false;
   }
   
   @Override
   public String toString() {
      return String.format("<%s>", name);
   }      
}
