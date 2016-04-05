package org.snapscript.develop.common;

import java.util.LinkedHashMap;
import java.util.Map;

import org.snapscript.parse.Grammar;
import org.snapscript.parse.GrammarCompiler;
import org.snapscript.parse.GrammarIndexer;
import org.snapscript.parse.GrammarResolver;
import org.snapscript.parse.Syntax;

public class TokenFinderBuilder {
   
   private final Map<String, Grammar> grammars;
   private final GrammarCompiler compiler;
   private final GrammarResolver resolver;
   private final GrammarIndexer indexer;
   private final TokenFinder finder;

   public TokenFinderBuilder() {
      this.grammars = new LinkedHashMap<String, Grammar>();      
      this.resolver = new GrammarResolver(grammars);
      this.indexer = new GrammarIndexer();
      this.finder = new TokenFinder(resolver, indexer);      
      this.compiler = new GrammarCompiler(resolver, indexer);      
   } 

   public synchronized TokenFinder compile() {
      if(grammars.isEmpty()) {
         Syntax[] language = Syntax.values();
         
         for(Syntax syntax : language) {
            String name = syntax.getName();
            String value = syntax.getGrammar();
            Grammar grammar = compiler.process(name, value);
            
            grammars.put(name, grammar);
         }
      }
      return finder;
   }
}
