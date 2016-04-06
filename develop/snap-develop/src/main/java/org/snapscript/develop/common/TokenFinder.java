package org.snapscript.develop.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.snapscript.parse.GrammarIndexer;
import org.snapscript.parse.GrammarResolver;
import org.snapscript.parse.SourceCode;
import org.snapscript.parse.SourceProcessor;
import org.snapscript.parse.Token;
import org.snapscript.parse.TokenIndexer;

public class TokenFinder {
   
   private static final String CLASS = "class";
   private static final String TRAIT = "trait";
   private static final String MODULE = "module";
   private static final String FUNCTION = "function";
   private static final String VARIABLE = "var";
   private static final String CONSTANT = "const";
   private static final String ENUMERATION = "enum";
   private static final String STRING = "string";
   private static final String TOKEN = "token";
   
   private final SourceProcessor processor;
   private final GrammarIndexer indexer;
   
   public TokenFinder(GrammarResolver resolver, GrammarIndexer indexer) {
      this.processor = new SourceProcessor(100);
      this.indexer = indexer;
   }
   
   public Map<String, String> findTokens(String source, String resource, String prefix) {
      List<Token> tokens = new ArrayList<Token>();
      Map<String, String> strings = new TreeMap<String,String>();
      
      if(!source.isEmpty()) {
         TokenIndexer indexer = createIndexer(source, resource);
         indexer.index(tokens);
      }
      String lowerCasePrefix = prefix.toLowerCase();
      int length = tokens.size();
      
      for(int i = 0; i < length; i++) {
         Token token = tokens.get(i);
         Object value = token.getValue();
         String text = String.valueOf(value);
         String lowerCaseText = text.toLowerCase();
         
         if(!prefix.equals(text) && lowerCaseText.startsWith(lowerCasePrefix)) {
            String type = TOKEN;
            
            if(isClass(tokens, i)) {
               type = CLASS;
            } else if(isTrait(tokens, i)) {
               type = TRAIT;
            } else if(isEnum(tokens, i)) {
               type = ENUMERATION;
            } else if(isModule(tokens, i)) {
               type = MODULE;
            } else if(isVariable(tokens, i)) {
               type = VARIABLE;
            } else if(isConstant(tokens, i)) {
               type = CONSTANT;
            } else if(isFunction(tokens, i)) {
               type = FUNCTION;
            } else if(isString(tokens, i)) {
               type = STRING;
            } 
            if(!type.equals(STRING)) {
               String previous = strings.get(text);
            
               if(previous == null || previous.equals(TOKEN)) {
                  if(type.equals(FUNCTION)) {
                     strings.put(text + "()", type);
                  } else {
                     strings.put(text, type);
                  }
               }
            }
         }
      }
      return strings;
   }
   
   private static boolean isClass(List<Token> tokens, int index) {
      if(index > 1) {
         String space = tokens.get(index-1).getValue().toString();
         String literal = tokens.get(index-2).getValue().toString();
         
         if(space.equals(" ") && literal.equals("class")) {
            return true;
         }
         if(space.equals(" ") && literal.equals("new")) {
            return true;
         }
      }
      return false;
   }
   
   private static boolean isEnum(List<Token> tokens, int index) {
      if(index > 1) {
         String space = tokens.get(index-1).getValue().toString();
         String literal = tokens.get(index-2).getValue().toString();
         
         if(space.equals(" ") && literal.equals("enum")) {
            return true;
         }
      }
      return false;
   }
   
   private static boolean isTrait(List<Token> tokens, int index) {
      if(index > 1) {
         String space = tokens.get(index-1).getValue().toString();
         String literal = tokens.get(index-2).getValue().toString();
         
         if(space.equals(" ") && literal.equals("trait")) {
            return true;
         }
         if(space.equals(" ") && literal.equals("with")) {
            return true;
         }
      }
      return false;
   }
   
   private static boolean isModule(List<Token> tokens, int index) {
      if(index > 1) {
         String space = tokens.get(index-1).getValue().toString();
         String literal = tokens.get(index-2).getValue().toString();
         
         if(space.equals(" ") && literal.equals("module")) {
            return true;
         }
      }
      return false;
   }
   
   private static boolean isVariable(List<Token> tokens, int index) {
      int length = tokens.size();
      
      if(index > 1) {
         String space = tokens.get(index-1).getValue().toString();
         String literal = tokens.get(index-2).getValue().toString();
         
         if(space.equals(" ") && literal.equals("var")) {
            return true;
         }
      }
      if(index + 1  < length) {
         String next = tokens.get(index+1).getValue().toString();
         
         if(next.equals(".")) {
            return true;
         }
      }
      return false;
   }
   
   private static boolean isConstant(List<Token> tokens, int index) {
      if(index > 1) {
         String space = tokens.get(index-1).getValue().toString();
         String literal = tokens.get(index-2).getValue().toString();
         
         if(space.equals(" ") && literal.equals("const")) {
            return true;
         }
      }
      return false;
   }
   
   private static boolean isFunction(List<Token> tokens, int index) {
      int length = tokens.size();
      
      if(index + 1  < length) {
         String brace = tokens.get(index+1).getValue().toString();
         
         if(brace.equals("(")) {
            return true;
         }
      }
      return false;
   }
   
   private static boolean isString(List<Token> tokens, int index) {
      int length = tokens.size();
      
      if(index + 1  < length) {
         String next = tokens.get(index+1).getValue().toString();
         
         if(next.equals("\"") || next.equals("\'")) {
            return true;
         }
      }
      return false;
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
