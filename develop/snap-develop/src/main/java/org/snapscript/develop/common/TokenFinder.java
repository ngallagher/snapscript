package org.snapscript.develop.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.snapscript.parse.GrammarIndexer;
import org.snapscript.parse.GrammarResolver;
import org.snapscript.parse.SourceCode;
import org.snapscript.parse.SourceProcessor;
import org.snapscript.parse.Token;
import org.snapscript.parse.TokenIndexer;

public class TokenFinder {
   
   private final SourceProcessor processor;
   private final GrammarIndexer indexer;
   
   public TokenFinder(GrammarResolver resolver, GrammarIndexer indexer) {
      this.processor = new SourceProcessor(100);
      this.indexer = indexer;
   }
   
   public Set<String> findTokens(String source, String resource, String prefix) {
      List<Token> tokens = new ArrayList<Token>();
      Set<String> strings = new TreeSet<String>();
      
      if(!source.isEmpty()) {
         TokenIndexer indexer = createIndexer(source, resource);
         indexer.index(tokens);
      }
      String lowerCasePrefix = prefix.toLowerCase();
      
      for(Token token : tokens) {
         Object value = token.getValue();
         String text = String.valueOf(value);
         String lowerCaseText = text.toLowerCase();
         
         if(!text.equals(prefix) && lowerCaseText.startsWith(lowerCasePrefix)) {
            strings.add(text);
         }
      }
      return strings;
   }
   
   public TokenIndexer createIndexer(String text, String resource) {
      char[] array = text.toCharArray();
      
      if(array.length > 0) {
         SourceCode source = processor.process(text);
         char[] original = source.getOriginal();
         char[] compress = source.getSource();
         short[] lines = source.getLines();
         short[]types = source.getTypes();
         
         return new TokenIndexer(indexer, resource, original, compress, lines, types);
      }
      return null;
   }
}
