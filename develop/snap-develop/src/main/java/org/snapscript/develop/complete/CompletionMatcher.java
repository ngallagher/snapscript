package org.snapscript.develop.complete;

import static org.snapscript.develop.complete.CompletionToken.CLASS;
import static org.snapscript.develop.complete.CompletionToken.CONSTANT;
import static org.snapscript.develop.complete.CompletionToken.ENUMERATION;
import static org.snapscript.develop.complete.CompletionToken.FUNCTION;
import static org.snapscript.develop.complete.CompletionToken.MODULE;
import static org.snapscript.develop.complete.CompletionToken.TOKEN;
import static org.snapscript.develop.complete.CompletionToken.TRAIT;
import static org.snapscript.develop.complete.CompletionToken.VARIABLE;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.snapscript.agent.ConsoleLogger;
import org.snapscript.core.Function;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Property;
import org.snapscript.parse.GrammarIndexer;
import org.snapscript.parse.GrammarResolver;
import org.snapscript.parse.SourceCode;
import org.snapscript.parse.SourceProcessor;
import org.snapscript.parse.Token;
import org.snapscript.parse.TokenIndexer;

public class CompletionMatcher {
   
   private final CompletionExpressionParser parser;
   private final CompletionTypeResolver resolver;
   private final SourceProcessor processor;
   private final GrammarIndexer indexer;
   
   public CompletionMatcher(GrammarResolver resolver, GrammarIndexer indexer, ConsoleLogger logger) {
      this.parser = new CompletionExpressionParser(logger);
      this.resolver = new CompletionTypeResolver(logger);
      this.processor = new SourceProcessor(100);
      this.indexer = indexer;
   }
   
   public Map<String, String> findTokens(File root, String source, String resource, String prefix, String complete) {
      Map<String, String> resultTokens = new TreeMap<String, String>();
      Map<String, CompletionType> types = resolver.resolveTypes(root, source, resource, prefix, complete);
      CompletionExpression expression = parser.parse(types, complete);
      CompletionType type = expression.getConstraint();
      
      //if(type == null) {
         Map<String, String> externalTokens = extractExternal(types, expression, source, resource, prefix);
         Map<String, String> internalTokens = extractInternal(types, expression, source, resource, prefix);
         
         resultTokens.putAll(externalTokens);
         resultTokens.putAll(internalTokens);
      //} else {
      //   // XXX this needs to consider the type hierarchy
      //   Map<String, String> constraintTokens = extractTypedTokens(type, expression, source, resource, prefix);
      //   resultTokens.putAll(constraintTokens);
      //}
      return resultTokens;
   }
   
   private Map<String, String> extractInternal(Map<String, CompletionType> types, CompletionExpression complete, String source, String resource, String prefix) {
      List<Token> tokens = new ArrayList<Token>();
      Map<String, String> strings = new HashMap<String,String>();
      CompletionFilter filter = new CompletionFilter(complete, prefix);
      
      if(!source.isEmpty()) {
         TokenIndexer indexer = createIndexer(source, resource);
         indexer.index(tokens);
      }
      int length = tokens.size();

      for(int i = 0; i < length; i++) {
         Token token = tokens.get(i);
         Object value = token.getValue();
         String text = String.valueOf(value);
         
         if(!prefix.equals(text) && text.startsWith(prefix)) {
            String type = CompletionTokenClassifier.classify(tokens, i);
            String previous = strings.get(text);
         
            if(previous == null || previous.equals(TOKEN)) {
               if(type.equals(TOKEN)) {
                  CompletionType match = types.get(text);
                  
                  if(match != null) {
                     if(match.isModule()) {
                        if(filter.acceptInternal(text, MODULE)) {
                           strings.put(text, MODULE);
                        }
                     } else {
                        if(filter.acceptInternal(text, CLASS)) {
                           strings.put(text, CLASS);
                        }
                     }
                  }
               } else {
                  if(filter.acceptInternal(text, type)) {
                     if(type.equals(FUNCTION)) {
                        strings.put(text + "()", type);
                     }  else {
                        strings.put(text, type);
                     }
                  }  
               }
            }
         }
      }
      return strings;
   }
   
   private Map<String, String> extractExternal(Map<String, CompletionType> types, CompletionExpression complete, String source, String resource, String prefix) {
      Map<String, String> strings = new HashMap<String,String>();
      Set<Entry<String, CompletionType>> entries = types.entrySet();
      
      for(Entry<String, CompletionType> entry : entries) {
         CompletionType type = entry.getValue();
         Map<String, String> result = extractTypedTokens(type, complete, source, resource, prefix);
         strings.putAll(result);
      }
      return strings;
   }
   
   private Map<String, String> extractTypedTokens(CompletionType type, CompletionExpression complete, String source, String resource, String prefix) {
      Map<String, String> strings = new HashMap<String,String>();
      CompletionFilter filter = new CompletionFilter(complete, prefix);
      List<Function> functions = type.getFunctions();
      List<Property> properties = type.getProperties();
      
      for(Function function : functions) {
         String name = function.getName();
         
         if(filter.acceptExternal(name, FUNCTION)) {
            strings.put(name + "()", FUNCTION);
         }
      }
      for(Property property : properties) {
         String name = property.getName();
         int modifiers = property.getModifiers();
         
         if(ModifierType.isConstant(modifiers)) {
            if(filter.acceptExternal(name, CONSTANT)) {
               strings.put(name, CONSTANT);
            }
         } else {
            if(filter.acceptExternal(name, VARIABLE)) {
               strings.put(name, VARIABLE);
            }
         }
      }
      String name = type.getName();
      Class real = type.getType();
      
      if(real != null) {
         if(real.isInterface()) {
            if(filter.acceptExternal(name, TRAIT)) {
               strings.put(name, TRAIT);
            }
         } else if(real.isEnum()){
            if(filter.acceptExternal(name, ENUMERATION)) {
               strings.put(name, ENUMERATION);
            }
         } else {
            if(filter.acceptExternal(name, CLASS)) {
               strings.put(name, CLASS);
            }
         }
      } else {
         if(filter.acceptExternal(name, CLASS)) {
            strings.put(name, CLASS);
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
