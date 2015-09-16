package org.snapscript.parse;

public class SyntaxParser {   
   
   private final SyntaxTreeBuilder builder;
   private final GrammarResolver resolver;
   
   public SyntaxParser(GrammarResolver resolver, GrammarIndexer indexer) {
      this.builder = new SyntaxTreeBuilder(indexer);
      this.resolver = resolver;
   }   

   public SyntaxNode parse(String expression, String name) {     
      Grammar grammar = resolver.resolve(name);
      
      if(grammar == null) {
         throw new IllegalArgumentException("Grammar " + name + " is not defined");
      }
      SyntaxTree tree = builder.create(expression, name);
      SyntaxReader root = tree.mark();
         
      if(grammar.read(root, 0)) {
         root.commit();
         return tree.commit();
      }
      return null;
   } 
}
