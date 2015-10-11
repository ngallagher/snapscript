package org.snapscript.parse;

import java.util.concurrent.atomic.AtomicInteger;

public class SyntaxTreeBuilder {

   private final SourceProcessor processor;
   private final GrammarIndexer indexer;
   private final AtomicInteger counter;
   
   public SyntaxTreeBuilder(GrammarIndexer indexer) {
      this.processor = new SourceProcessor(100);
      this.counter = new AtomicInteger(1);
      this.indexer = indexer;
   }

   public SyntaxTree create(String text, String grammar) {
      int serial = counter.getAndIncrement();
      char[] array = text.toCharArray();
      
      if(array.length == 0) {
         throw new IllegalStateException("Source text is empty for '" + grammar + "'");
      }
      SourceCode source = processor.process(text);
      char[] data = source.getSource();
      short[] lines = source.getLines();
      short[]types = source.getTypes();
      int length = data.length;

      return new SyntaxTree(indexer, grammar, data, lines, types, 0, length, serial);
   }       
}

