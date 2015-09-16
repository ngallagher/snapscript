package org.snapscript.parse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

import junit.framework.TestCase;

public class ConstructEvaluationTest extends TestCase {
   
   public static interface Scope{
      Object get(String x);
   }
   public static interface Evaluation<T> {
      T evaluate(Scope scope);
   }
   public static class LiteralEvaluation implements Evaluation {
      
      private final Object literal;
      
      public LiteralEvaluation(Object literal){
         this.literal = literal;
      }

      @Override
      public Object evaluate(Scope scope) {        
         return literal;
      }      
   }
   public static class InvocationEvaluation implements Evaluation {
      
      private final Evaluation[] arguments;
      private final String name;
      
      public InvocationEvaluation(String name, Evaluation[] arguments) {
         this.arguments = arguments;
         this.name = name;
      }

      @Override
      public Object evaluate(Scope scope) {        
         return null;
      }      
   }
   public static class IndexEvaluation implements Evaluation {
      
      private final Evaluation argument;
      private final String name;
      
      public IndexEvaluation(String name, Evaluation argument) {
         this.argument = argument;
         this.name = name;
      }

      @Override
      public Object evaluate(Scope scope) {
         Object index = argument.evaluate(scope);
         Object array = scope.get(name);         
         
         return Array.get(array, (Integer)index);
      }      
   } 
   
   public static class EvaluationAssembler {
      
      private final Map<String, Class> types;
      
      public EvaluationAssembler(Map<String, Class> types) {
         this.types = types;
      }      
      
      public Evaluation assemble(List<SyntaxNode> list) {
         Iterator<SyntaxNode> iterator = list.iterator();
         
         while(iterator.hasNext()) {
            SyntaxNode token = iterator.next();
            String grammar = token.getGrammar();
            Class type = types.get(grammar);
            
            if(type != null) {
               
            }            
         }
         return null;
      }
   }
   
   /*
     
    There needs to be a concept that when you cross a certain expression, e.g "reference-expression"
    then you must return a specific Evaluation implementation from it, with a specific set of 
    parameters.
    
    
    interface Evaluation {
       Reference evaluate(Scope scope); // return reference to the result of the evaluation
    }
    
    function-invocation -> [method] [argument-list]  InvocationEvaluation(String method, Evaluation[] arguments)
    variable-reference -> [variable] VariableEvaluation(String variable)
    array-index -> [array] [argument] IndexEvaluation(String array, Evaluation argument)
    literal -> [number|text|null|booeal] NumberEvaluation(Number value), TextEvaluation(String text)
    
    postfix-decrement-expression -> PosfixDecrementEvaluation(String variable); !!!!!!!!!!!!!!!!!!! what if its a reference-chain like document.getBlah().innerValue
    postfix-increment-expression -> PosfixIncrementEvaluation(String variable);
    
    
     reference --> [variable.method(1,array[12],b.invoke("method"))]
      reference-expression --> [variable.method(1,array[12],b.invoke("method"))]
       reference-expression --> [variable.method(1,array[12],b.invoke("method"))]
        reference-handle --> [variable]
         reference-handle --> [variable]
          variable-reference --> [variable]
           variable --> [variable] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> return [Variable] 
        reference-expression --> [.method(1,array[12],b.invoke("method"))]
         reference-expression --> [.method(1,array[12],b.invoke("method"))]
          reference-expression --> [method(1,array[12],b.invoke("method"))]
           reference-expression --> [method(1,array[12],b.invoke("method"))]
            reference-handle --> [method(1,array[12],b.invoke("method"))]
             reference-handle --> [method(1,array[12],b.invoke("method"))]
              function-invocation --> [method(1,array[12],b.invoke("method"))]
               function-invocation --> [method(1,array[12],b.invoke("method"))]
                function --> [method] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 
                function-invocation --> [1,array[12],b.invoke("method")]
                 argument-list --> [1,array[12],b.invoke("method")]
                  argument-list --> [1,array[12],b.invoke("method")]
                   argument --> [1]
                    argument --> [1]
                     literal --> [1]
                      literal --> [1]
                       number --> [1]
                        number --> [1]
                   argument-list --> [,array[12],b.invoke("method")]
                    argument-list --> [,array[12]]
                     argument --> [array[12]]
                      argument --> [array[12]]
                       reference --> [array[12]]
                        reference-expression --> [array[12]]
                         reference-expression --> [array[12]]
                          reference-handle --> [array[12]]
                           reference-handle --> [array[12]]
                            array-index --> [array[12]]
                             array-index --> [array[12]]
                              array --> [array]
                              argument --> [12]
                               argument --> [12]
                                literal --> [12]
                                 literal --> [12]
                                  number --> [12]
                                   number --> [12]
                                   
                                   
                                   
     In a ReferenceEvaluation the referenced value is always this, unless there is a variable,
     this makes it easier when invoking methods.
      
    
    */
   public void testGrammarTree() throws Exception {
      //parse("variable.method(1+i,array[12.0f],b.invoke(\"method\"))", "expression", "reference-handle", "variable");   
      //parse("variable.method(1+i,array[12.0f],b.invoke(\"method\"))", "expression", "function-invocation", "function");   
      //parse("variable.method(1+i,array[12.0f],b.invoke(\"method\"))", "expression", "argument-list", "argument");     
   }
   
   public void parse(String expression, String root, String parent, String child) throws Exception {
      List<String> list = match(expression, root, parent, child);

      System.err.println("#######" + expression + "########");
      
      for(String next : list) {
         System.err.printf(next);
      }  
      System.err.println();
   }
   
   public List<String> match(String source, String root, String parent, String child) throws Exception {
      SyntaxParser lexer = LexerBuilder.create();      
      LexerBuilder.print(lexer, source, root);
      
      SyntaxNode node = lexer.parse(source, root);

      if(node != null) {
         return match(node, parent, child, 0);
      }      
      return Collections.emptyList();
   }
   
   public List<String> match(SyntaxNode token, String parent, String child, int depth){
      String name = token.getGrammar();
      List<SyntaxNode> tokens = token.getNodes();
      Iterator<SyntaxNode> iterator = tokens.iterator();
      int count = 0;
      
      if(name.equals(parent)) {
         List<String> list = new ArrayList<String>();
      
         while(iterator.hasNext()) {
            SyntaxNode next = iterator.next();
            String grammar = next.getGrammar();
            
            if(grammar.equals(parent)) {
               List<String> values = match(next, parent, child, depth + 1); // dive in deeper
               
               if(values != null) {
                  list.addAll(values);
               }
            }
            if(grammar.equals(child)) {
               String source = next.getSource();
               String value = String.format("%s-%s %s --> [%s]%n", depth, count++, grammar, source);
               
               list.add(value);
            }
         }
         return list;
      } else {
         while(iterator.hasNext()) {
            SyntaxNode next = iterator.next();
            List<String> list = match(next, parent, child, depth + 1);
            
            if(list != null) {
               return list;
            }
         }
      } 
      return null;
   }
}
