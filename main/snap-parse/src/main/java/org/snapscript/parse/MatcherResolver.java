package org.snapscript.parse;

import java.util.concurrent.atomic.AtomicReference;

public class MatcherResolver {

   private final GrammarResolver resolver; 
   private final String name;
   private final int index;
   
   public MatcherResolver(GrammarResolver resolver, String name, int index) {
      this.resolver = resolver;
      this.index = index;
      this.name = name;
   }   
   
   public Matcher resolve(int serial) {
      Grammar grammar = resolver.resolve(name);
      
      if(grammar == null) {
         throw new ParseException("Grammar '" + name + "' not found");
      }
      return new GrammarMatcher(grammar, name, index, serial);
   }  
   
   private static class GrammarMatcher implements Matcher {
      
      private final AtomicReference<Matcher> reference;
      private final Grammar grammar;
      private final String name;
      private final int index;
      private final int serial;
      
      public GrammarMatcher(Grammar grammar, String name, int index, int serial) {
         this.reference = new AtomicReference<Matcher>();
         this.grammar = grammar;
         this.serial = serial;
         this.index = index;
         this.name = name;
      }  
   
      @Override
      public boolean match(SyntaxReader node, int depth) {  
         Matcher matcher = reference.get();
         
         if(matcher == null) {
            matcher = grammar.compile(serial);
            reference.set(matcher);
         }
         SyntaxReader child = node.mark(index);   
   
         if(child != null) {
            if(matcher.match(child, 0)) {
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
}
