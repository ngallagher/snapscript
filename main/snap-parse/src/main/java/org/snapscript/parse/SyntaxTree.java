package org.snapscript.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SyntaxTree {

   private final Comparator<SyntaxNode> comparator;
   private final Series<SyntaxCursor> nodes;
   private final LexicalAnalyzer analyzer;
   private final GrammarIndexer indexer;
   private final AtomicInteger commit;
   private final IntegerStack stack;
   private final String resource;
   private final String grammar;
   private final long serial;

   public SyntaxTree(GrammarIndexer indexer, String resource, String grammar, char[] original, char[] source, short[] lines, short[] types, int serial) {
      this.analyzer = new TokenScanner(indexer, resource, original, source, lines, types);
      this.comparator = new SyntaxNodeComparator();
      this.nodes = new Series<SyntaxCursor>();
      this.commit = new AtomicInteger();
      this.stack = new IntegerStack();
      this.resource = resource;
      this.indexer = indexer;
      this.grammar = grammar;
      this.serial = serial;
   } 

   public SyntaxReader mark() {   
      int index = indexer.index(grammar);
      int depth = stack.depth(index);

      if (depth >= 0) {
         throw new ParseException("Tree has been created");
      }
      stack.push(index);
      return new SyntaxCursor(nodes, index, index, 0);
   }
   
   public SyntaxNode commit() { 
      int size = nodes.size();
      
      if(size > 2) {
         throw new ParseException("Tree has more than one root");
      }
      int mark = analyzer.mark();
      int count = analyzer.count();
      
      if(mark != count) {
         int error = commit.get(); // last successful commit
         Line line = analyzer.line(error);
         
         if(resource != null) {
            throw new ParseException("Syntax error in '" + resource + "' at line " + line);
         }  
         throw new ParseException("Syntax error at line " + line);
      }
      return create();
   }
   
   public SyntaxNode create() {
      int size = nodes.size();
      
      if(size > 2) {
         throw new ParseException("Tree has more than one root");
      }
      SyntaxCursor cursor = nodes.get(0);
      SyntaxNode node = cursor.create();
      
      if(node == null) {
         throw new ParseException("Tree has no root");
      }
      return node;
   }

   private class SyntaxCursor implements SyntaxReader {

      private Series<SyntaxCursor> parent;
      private Series<SyntaxCursor> nodes;
      private Token value;
      private int grammar;
      private int key;
      private int start;

      public SyntaxCursor(Series<SyntaxCursor> parent, int grammar, int key, int start) {
         this.nodes = new Series<SyntaxCursor>();
         this.grammar = grammar;
         this.parent = parent;
         this.start = start;
         this.key = key;
      }      
      
      public SyntaxNode create() {
         return new SyntaxResult(nodes, value, grammar, start);
      }

      @Override
      public long position() {
         return (serial << 32) | analyzer.mark();
      }

      @Override
      public SyntaxReader mark(int grammar) {              
         int off = analyzer.mark();
         int key = off << 10 | grammar;
         int index = stack.depth(key); // this is slow!!

         if (index <= 0) {
            stack.push(key);
            return new SyntaxCursor(nodes, grammar, key, off);
         }
         return null;
      }    

      @Override
      public int reset() {
         int mark = analyzer.mark();
         
         while (!stack.isEmpty()) {
            int top = stack.pop();

            if (top == key) {
               break;
            }
         }
         analyzer.reset(start); // sets the global offset
         return mark;
      }

      @Override
      public void commit() {
         int mark = analyzer.mark();
         int error = commit.get();
         
         while (!stack.isEmpty()) {
            int top = stack.pop();

            if (top == key) {
               if(mark > error) {
                  commit.set(mark);
               }
               parent.add(this);
               break;
            }
         }
      }

      @Override
      public boolean literal(String text) {
         Token token = analyzer.literal(text);

         if (token != null) {
            value = token;
            return true;
         }
         return false;
      }

      @Override
      public boolean decimal() {
         Token token = analyzer.decimal();

         if (token != null) {
            value = token;
            return true;
         }
         return false;
      }
      
      @Override
      public boolean binary() {
         Token token = analyzer.binary();

         if (token != null) {
            value = token;
            return true;
         }
         return false;
      }

      @Override
      public boolean hexidecimal() {
         Token token = analyzer.hexidecimal();

         if (token != null) {
            value = token;
            return true;
         }
         return false;
      }

      @Override
      public boolean identifier() {
         Token token = analyzer.identifier();

         if (token != null) {
            value = token;
            return true;
         }
         return false;
      }
      
      @Override
      public boolean qualifier() {
         Token token = analyzer.qualifier();

         if (token != null) {
            value = token;
            return true;
         }
         return false;
      }     
      
      @Override
      public boolean type() {
         Token token = analyzer.type();

         if (token != null) {
            value = token;
            return true;
         }
         return false;
      }       

      @Override
      public boolean text() {
         Token token = analyzer.text();

         if (token != null) {
            value = token;
            return true;
         }
         return false;
      }
      
      @Override
      public boolean template() {
         Token token = analyzer.template();

         if (token != null) {
            value = token;
            return true;
         }
         return false;
      }
   }

   private class SyntaxResult implements SyntaxNode {

      private Series<SyntaxCursor> children;
      private Token token;
      private int grammar;
      private int start;

      public SyntaxResult(Series<SyntaxCursor> children, Token token, int grammar, int start) {
         this.children = children;
         this.grammar = grammar;
         this.token = token;
         this.start = start;
      }

      @Override
      public List<SyntaxNode> getNodes() {
         int size = children.size();
         
         if(size > 0) {
            List<SyntaxNode> result = new ArrayList<SyntaxNode>(size);
            
            for(int i = 0; i < size; i++) {
               SyntaxCursor child = children.get(i);
               SyntaxNode node = child.create();
               
               if(node != null) {
                  result.add(node);
               }
            }         
            if(size > 1) {
               Collections.sort(result, comparator);
            }
            return result;
         }
         return Collections.emptyList();
      }

      @Override
      public String getGrammar() {
         return indexer.value(grammar);
      }
      
      @Override
      public Line getLine() {
         return analyzer.line(start);
      }

      @Override
      public Token getToken() {
         return token;
      }

      @Override
      public int getStart() {
         return start;
      }
   } 
}
