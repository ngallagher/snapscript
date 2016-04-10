package org.snapscript.develop.complete;

import static org.snapscript.develop.complete.CompletionToken.CLASS;
import static org.snapscript.develop.complete.CompletionToken.CONSTANT;
import static org.snapscript.develop.complete.CompletionToken.ENUMERATION;
import static org.snapscript.develop.complete.CompletionToken.FUNCTION;
import static org.snapscript.develop.complete.CompletionToken.MODULE;
import static org.snapscript.develop.complete.CompletionToken.TRAIT;
import static org.snapscript.develop.complete.CompletionToken.VARIABLE;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.snapscript.agent.ConsoleLogger;
import org.snapscript.core.Function;
import org.snapscript.core.ModifierType;
import org.snapscript.core.PathConverter;
import org.snapscript.core.Property;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;
import org.snapscript.parse.GrammarIndexer;
import org.snapscript.parse.GrammarResolver;

public class CompletionMatcher {
   
   private final CompletionContextExtractor extractor;
   private final CompletionExpressionParser parser;
   private final CompletionTypeResolver resolver;
   private final PathConverter converter;
   private final ConsoleLogger logger;
   
   public CompletionMatcher(GrammarResolver resolver, GrammarIndexer indexer, ConsoleLogger logger) {
      this.extractor = new CompletionContextExtractor(resolver, indexer);
      this.parser = new CompletionExpressionParser(logger);
      this.resolver = new CompletionTypeResolver(logger);
      this.converter = new PathConverter();
      this.logger = logger;
   }
   
   public Map<String, String> findTokens(File root, String source, String resource, String prefix, String complete, int line) {
      Map<String, String> resultTokens = new TreeMap<String, String>();
      long start = System.currentTimeMillis();
      
      try {
         Map<String, CompletionType> types = resolver.resolveTypes(root, source, resource, prefix, complete, line);
         CompletionContext context = extractor.extractContext(types, source, resource, prefix, line);
         CompletionExpression expression = parser.parse(types, context, complete);
         CompletionType type = expression.getConstraint();
         String module = converter.createModule(resource);
         
         if(type != null) {
            Map<String, String> availableTokens = extractTypeTokens(type, expression, source, resource, prefix);
            resultTokens.putAll(availableTokens);
         }
         CompletionType thisType = context.getType();
         CompletionType thisModule = types.get(module);
         Map<String, String> thisTokens = context.getTokens();
         
         if(thisType != null) {
            Map<String, String> availableTokens = extractTypeTokens(thisType, expression, source, resource, prefix);
            resultTokens.putAll(availableTokens);
         }
         if(thisModule != null) {
            Map<String, String> availableTokens = extractTypeTokens(thisModule, expression, source, resource, prefix);
            resultTokens.putAll(availableTokens);
         }
         Map<String, String> globalTokens = extractGlobalTokens(types, expression, source, resource, prefix);
         resultTokens.putAll(globalTokens);
         resultTokens.putAll(thisTokens);
         return resultTokens;
      } finally {
         long finish = System.currentTimeMillis();
         long duration = finish - start;
         
         logger.log("Time taken to find tokens " + duration);
      }
   }
   
   private Map<String, String> extractTypeTokens(CompletionType type, CompletionExpression complete, String source, String resource, String prefix) {
      Map<String, String> strings = new HashMap<String,String>();
      CompletionFilter filter = new CompletionFilter(complete, prefix);
      List<Function> functions = type.getFunctions();
      List<Property> properties = type.getProperties();
      
      for(Function function : functions) {
         String name = function.getName();
         
         if(filter.acceptToken(name, FUNCTION)) {
            Signature signature = function.getSignature();
            List<String> parameters = signature.getNames();
            Type constraint = function.getConstraint(); // perhaps add the return type
            int count = parameters.size();
            
            if(count > 0) {
               StringBuilder builder = new StringBuilder();
               
               for(int i = 0; i < count; i++) {
                  String parameter = parameters.get(i);
                  
                  if(i > 0) {
                     builder.append(", ");
                  }
                  builder.append(parameter);
               }
               strings.put(name + "(" + builder + ")", FUNCTION);
            } else {
               strings.put(name + "()", FUNCTION);
            }
         }
      }
      for(Property property : properties) {
         String name = property.getName();
         int modifiers = property.getModifiers();
         
         if(ModifierType.isConstant(modifiers)) {
            if(filter.acceptToken(name, CONSTANT)) {
               strings.put(name, CONSTANT);
            }
         } else {
            if(filter.acceptToken(name, VARIABLE)) {
               strings.put(name, VARIABLE);
            }
         }
      }
      return strings;
   }
   
   private Map<String, String> extractGlobalTokens(Map<String, CompletionType> types, CompletionExpression complete, String source, String resource, String prefix) {
      Map<String, String> strings = new HashMap<String,String>();
      CompletionFilter filter = new CompletionFilter(complete, prefix);
      Set<String> names = types.keySet();
      
      for(String key : names) {
         CompletionType type = types.get(key);
         String name = type.getName();
         Class real = type.getType();
         
         if(real != null) {
            if(!real.isArray()) {
               if(real.isInterface()) {
                  if(filter.acceptToken(name, TRAIT)) {
                     strings.put(name, TRAIT);
                  }
               } else if(real.isEnum()){
                  if(filter.acceptToken(name, ENUMERATION)) {
                     strings.put(name, ENUMERATION);
                  }
               } else {
                  if(filter.acceptToken(name, CLASS)) {
                     strings.put(name, CLASS);
                  }
               }
            }
         } else {
            if(type.isType()) {
               if(filter.acceptToken(name, CLASS)) {
                  strings.put(name, CLASS);
               }
            } else if(type.isModule()) {
               if(filter.acceptToken(name, MODULE)) {
                  strings.put(name, MODULE);
               }
            }
         }
      }
      return strings;
   }
}
