package org.snapscript.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SyntaxTree {

   private final Comparator<SyntaxNode> comparator;
   private final List<SyntaxCursor> nodes;
   private final LexicalAnalyzer analyzer;
   private final GrammarIndexer indexer;
   private final IntegerStack stack;
   private final String grammar;
   private final long serial;

   public SyntaxTree(GrammarIndexer indexer, String grammar, char[] original, char[] source, short[] lines, short[] types, int serial) {
      this.analyzer = new TokenScanner(indexer, original, source, lines, types);
      this.comparator = new SyntaxNodeComparator();
      this.nodes = new ArrayList<SyntaxCursor>();
      this.stack = new IntegerStack();
      this.indexer = indexer;
      this.grammar = grammar;
      this.serial = serial;
   } 

   public SyntaxReader mark() {   
      int index = indexer.index(grammar);
      int depth = stack.depth(index);

      if (depth >= 0) {
         throw new IllegalStateException("Tree has been created");
      }
      stack.push(index);
      return new SyntaxCursor(nodes, index, index, 0, 0);
   }
   
   public SyntaxNode commit() { 
      int size = nodes.size();
      
      if(size > 2) {
         throw new IllegalStateException("Tree has more than one root");
      }
      int mark = analyzer.mark();
      int count = analyzer.count();
      
      if(mark != count) {
         Line line = analyzer.line(mark);
         
         if(size <= 1) {
            throw new IllegalStateException("Syntax error in source at line " + line);
         }  
      }
      return create();
   }
   
   public SyntaxNode create() {
      int size = nodes.size();
      
      if(size > 2) {
         throw new IllegalStateException("Tree has more than one root");
      }
      SyntaxCursor cursor = nodes.get(0);
      SyntaxNode node = cursor.create();
      
      if(node == null) {
         throw new IllegalStateException("Tree has no root");
      }
      return node;
   }
 
   private class SyntaxCursor implements SyntaxReader {

      private List<SyntaxCursor> parent;
      private List<SyntaxCursor> nodes;
      private Token value;
      private int grammar;
      private int mark;
      private int key;
      private int start;
      private int depth;

      public SyntaxCursor(List<SyntaxCursor> parent, int grammar, int key, int start, int depth) {
         this.nodes = new ArrayList<SyntaxCursor>();
         this.grammar = grammar;
         this.parent = parent;
         this.start = start;
         this.depth = depth;
         this.key = key;
      }      
      
      public SyntaxNode create() {
         return new SyntaxResult(nodes, value, grammar, start, depth);
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
            return new SyntaxCursor(nodes, grammar, key, off, depth + 1);
         }
         return null;
      }    

      @Override
      public int reset() {
         int current = analyzer.mark();
         
         while (!stack.isEmpty()) {
            int top = stack.pop();

            if (top == key) {
               break;
            }
         }
         analyzer.reset(start); // sets the global offset
         return current;
      }

      @Override
      public void commit() {
         int current = analyzer.mark();
         
         while (!stack.isEmpty()) {
            int top = stack.pop();

            if (top == key) {
               parent.add(this);
               mark = current;
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

      private List<SyntaxCursor> children;
      private Token token;
      private int grammar;
      private int start;
      private int depth;

      public SyntaxResult(List<SyntaxCursor> children, Token token, int grammar, int start, int depth) {
         this.children = children;
         this.grammar = grammar;
         this.token = token;
         this.depth = depth;
         this.start = start;
      }

      @Override
      public List<SyntaxNode> getNodes() {
         int size = children.size();
         
         if(size > 0) {
            List<SyntaxNode> result = new ArrayList<SyntaxNode>();
            
            for(SyntaxCursor child : children) {
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
      public Token getToken() {
         return token;
      }

      @Override
      public int getDepth() {
         return depth;
      }

      @Override
      public int getStart() {
         return start;
      }
   } 
}
