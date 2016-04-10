package org.snapscript.develop.complete;

import static org.snapscript.develop.complete.CompletionToken.CLASS;
import static org.snapscript.develop.complete.CompletionToken.CONSTANT;
import static org.snapscript.develop.complete.CompletionToken.ENUMERATION;
import static org.snapscript.develop.complete.CompletionToken.FUNCTION;
import static org.snapscript.develop.complete.CompletionToken.TRAIT;
import static org.snapscript.develop.complete.CompletionToken.VARIABLE;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.snapscript.agent.ConsoleLogger;
import org.snapscript.core.Function;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Property;
import org.snapscript.parse.GrammarIndexer;
import org.snapscript.parse.GrammarResolver;

public class CompletionMatcher {
   
   private final CompletionContextExtractor extractor;
   private final CompletionExpressionParser parser;
   private final CompletionTypeResolver resolver;
   
   public CompletionMatcher(GrammarResolver resolver, GrammarIndexer indexer, ConsoleLogger logger) {
      this.extractor = new CompletionContextExtractor(resolver, indexer);
      this.parser = new CompletionExpressionParser(logger);
      this.resolver = new CompletionTypeResolver(logger);
   }
   
   public Map<String, String> findTokens(File root, String source, String resource, String prefix, String complete, int line) {
      Map<String, String> resultTokens = new TreeMap<String, String>();
      Map<String, CompletionType> types = resolver.resolveTypes(root, source, resource, prefix, complete, line);
      CompletionType context = extractor.extractType(types, source, resource, prefix, line);
      CompletionExpression expression = parser.parse(types, context, complete);
      CompletionType type = expression.getConstraint();
      
      if(type != null) {
         Map<String, String> tokens = extractTokens(type, expression, source, resource, prefix);
         resultTokens.putAll(tokens);
      }
      if(context != null) {
         Map<String, String> tokens = extractTokens(context, expression, source, resource, prefix);
         resultTokens.putAll(tokens);
      }
      return resultTokens;
   }
   
   private Map<String, String> extractTokens(CompletionType type, CompletionExpression complete, String source, String resource, String prefix) {
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
}
