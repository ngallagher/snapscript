package org.snapscript.develop.complete;

import static org.snapscript.develop.complete.CompletionBraceCounter.CLOSE_BRACE;
import static org.snapscript.develop.complete.CompletionBraceCounter.OPEN_BRACE;
import static org.snapscript.develop.complete.CompletionToken.CLASS;
import static org.snapscript.develop.complete.CompletionToken.ENUMERATION;
import static org.snapscript.develop.complete.CompletionToken.MODULE;
import static org.snapscript.develop.complete.CompletionToken.TRAIT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.snapscript.parse.GrammarIndexer;
import org.snapscript.parse.GrammarResolver;
import org.snapscript.parse.Line;
import org.snapscript.parse.SourceCode;
import org.snapscript.parse.SourceProcessor;
import org.snapscript.parse.Token;
import org.snapscript.parse.TokenIndexer;

public class CompletionContextExtractor {

   private final CompletionBraceCounter counter;
   private final SourceProcessor processor;
   private final GrammarIndexer indexer;
   
   public CompletionContextExtractor(GrammarResolver resolver, GrammarIndexer indexer) {
      this.counter = new CompletionBraceCounter();
      this.processor = new SourceProcessor(100);
      this.indexer = indexer;
   }
   
   public CompletionType extractType(Map<String, CompletionType> types, String source, String resource, String prefix, int line) {
      List<Token> tokens = new ArrayList<Token>();

      if(!source.isEmpty()) {
         TokenIndexer indexer = createIndexer(source, resource);
         indexer.index(tokens);
      }
      int length = tokens.size();

      for(int i = 0; i < length; i++) {
         Token token = tokens.get(i);
         Object value = token.getValue();
         String text = String.valueOf(value);
         String context = counter.getType();
         Line current = token.getLine();
         int number = current.getNumber();
         
         if(number > line) {
            return types.get(context);
         }
         if(text.equals(OPEN_BRACE) || text.equals(CLOSE_BRACE)) {
            counter.setBrace(text);
         }
         String type = CompletionTokenClassifier.classify(tokens, i);
      
         if(type.equals(CLASS) || type.equals(TRAIT) || type.equals(ENUMERATION) || type.equals(MODULE)) {
            counter.setType(text);
         }
      }
      return null;
   }
   
   private TokenIndexer createIndexer(String text, String resource) {
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
