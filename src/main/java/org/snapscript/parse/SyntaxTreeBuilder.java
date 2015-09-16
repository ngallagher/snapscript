package org.snapscript.parse;

import static org.snapscript.parse.TextCategory.INDEX;

import java.util.concurrent.atomic.AtomicInteger;

public class SyntaxTreeBuilder {

   private final TextCompressor compressor;
   private final GrammarIndexer indexer;
   private final AtomicInteger counter;
   
   public SyntaxTreeBuilder(GrammarIndexer indexer) {
      this.compressor = new TextCompressor();
      this.counter = new AtomicInteger(1);
      this.indexer = indexer;
   }

   public SyntaxTree create(String text, String grammar) {
      TextSource source = compressor.compress(text);
      char[] data = source.getSource();
      short[] lines = source.getLines();
      int serial = counter.getAndIncrement();
      int length = data.length;

      if(length == 0) {
         throw new IllegalArgumentException("Source was empty for '" + grammar + "'");
      }
      short[] types = new short[length];
      
      for(int i = 0; i < length; i++) {
         char next = data[i];
         
         if(next < INDEX.length) {
            types[i] = INDEX[next];
         }
      }
      return new SyntaxTree(indexer, grammar, data, lines, types, 0, length, serial);
   }       
}

