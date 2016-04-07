package org.snapscript.develop.complete;

import static org.snapscript.develop.complete.CompletionTokenClassifier.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.snapscript.agent.ConsoleLogger;
import org.snapscript.core.Function;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Property;
import org.snapscript.core.Type;
import org.snapscript.parse.GrammarIndexer;
import org.snapscript.parse.GrammarResolver;
import org.snapscript.parse.SourceCode;
import org.snapscript.parse.SourceProcessor;
import org.snapscript.parse.Token;
import org.snapscript.parse.TokenIndexer;

public class CompletionMatcher {
   
   private final CompletionTypeResolver resolver;
   private final SourceProcessor processor;
   private final GrammarIndexer indexer;
   
   public CompletionMatcher(GrammarResolver resolver, GrammarIndexer indexer, ConsoleLogger logger) {
      this.resolver = new CompletionTypeResolver(logger);
      this.processor = new SourceProcessor(100);
      this.indexer = indexer;
   }
   
   public Map<String, String> findTokens(File root, String source, String resource, String prefix, String complete) {
      Map<String, Type> types = resolver.resolveTypes(root, source, resource);
      Map<String, String> externalTokens = extractExternal(types, source, resource, prefix, complete);
      Map<String, String> internalTokens = extractInternal(types, source, resource, prefix, complete);
      
      externalTokens.putAll(internalTokens);
      
      return externalTokens;
   }
   
   private Map<String, String> extractInternal(Map<String, Type> types, String source, String resource, String prefix, String complete) {
      List<Token> tokens = new ArrayList<Token>();
      Map<String, String> strings = new TreeMap<String,String>();
      CompleteFilter filter = new CompleteFilter(prefix, complete);
      
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
                  Type match = types.get(text);
                  
                  if(match != null) {
                     if(filter.acceptInternal(text, CLASS)) {
                        strings.put(text, CLASS);
                     }
                  }
               } else {
                  if(filter.acceptInternal(text, type)) {
                     strings.put(text, type);
                  }  
               }
            }
         }
      }
      return strings;
   }
   
   private Map<String, String> extractExternal(Map<String, Type> types, String source, String resource, String prefix, String complete) {
      Map<String, String> strings = new TreeMap<String,String>();
      CompleteFilter filter = new CompleteFilter(prefix, complete);
      Set<Entry<String, Type>> entries = types.entrySet();
      
      for(Entry<String, Type> entry : entries) {
         Type type = entry.getValue();
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
