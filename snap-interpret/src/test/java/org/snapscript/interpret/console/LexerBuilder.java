package org.snapscript.interpret.console;

import java.util.List;

import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;
import org.snapscript.parse.Token;

public class LexerBuilder {

   public static SyntaxParser create(){
      SyntaxCompiler builder = new SyntaxCompiler();
      return builder.compile();
   }
  
   public static void print(SyntaxParser analyzer, String source, String name) throws Exception {
      SyntaxNode node=analyzer.parse(source, name);
      print(node, 0, 0);             
      System.err.println();
      System.err.println();
   }
   
   public static void print(SyntaxNode token, int parent, int depth) throws Exception{   
      String grammar = token.getGrammar();
      List<SyntaxNode> children = token.getNodes();
      Token t = token.getToken();
      
      for (int i = 0; i < depth; i++) {
         System.err.print(" ");
      }      
      System.err.printf("%s --> [%s] --> (", grammar, token.getSource());
      int count = 0;
      
      for(SyntaxNode next : children) { 
         String child = next.getGrammar();
         
         if(count++ != 0) {
            System.err.print(", ");
         }
         System.err.printf(child);
      }
      System.err.print(")");
      
      if(t != null) {
         System.err.print(" = \"");
         System.err.print(t.getValue());
         System.err.print("\" ");
         System.err.print(t.getValue().getClass().getSimpleName());
      }
      System.err.println();
      System.err.flush();
      
      for(SyntaxNode next : children) {   
         String child = next.getGrammar();
         
         
         //if(top.contains(child)) {
         if(child.equals(grammar)) {
            print(next, System.identityHashCode(token), depth); // no depth change with no grammar change
         } else {
            print(next, System.identityHashCode(token), depth+2);
         }
         //}
        // }else {
            //System.err.println(next + " "+child.hasNext() + " "+iterator.ready());
          //  printStructure(child, top, depth); // stay at same depth
         //}
      }
   }  

}
