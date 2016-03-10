package org.snapscript.parse;

import java.util.concurrent.atomic.AtomicInteger;

public class SyntaxParser {   
   
   private final SyntaxTreeBuilder builder;
   private final GrammarResolver resolver;
   private final AtomicInteger counter;
   
   public SyntaxParser(GrammarResolver resolver, GrammarIndexer indexer) {
      this.builder = new SyntaxTreeBuilder(indexer);
      this.counter = new AtomicInteger();
      this.resolver = resolver;
   }   

   public SyntaxNode parse(String resource, String expression, String name) {     
      Grammar grammar = resolver.resolve(name);
      int count = counter.getAndIncrement();
      
      if(grammar == null) {
         throw new IllegalArgumentException("Grammar " + name + " is not defined");
      }
      Matcher matcher = grammar.compile(count);
      SyntaxTree tree = builder.create(resource, expression, name);
      SyntaxReader root = tree.mark();
         
      if(matcher.match(root, 0)) {
         root.commit();
         return tree.commit();
      }
      return null;
   } 
}
