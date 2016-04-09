package org.snapscript.develop.complete;

import static org.snapscript.develop.complete.CompletionToken.CLASS;
import static org.snapscript.develop.complete.CompletionToken.CONSTANT;
import static org.snapscript.develop.complete.CompletionToken.ENUMERATION;
import static org.snapscript.develop.complete.CompletionToken.FUNCTION;
import static org.snapscript.develop.complete.CompletionToken.MODULE;
import static org.snapscript.develop.complete.CompletionToken.TOKEN;
import static org.snapscript.develop.complete.CompletionToken.TRAIT;
import static org.snapscript.develop.complete.CompletionToken.VARIABLE;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.snapscript.agent.ConsoleLogger;

public class CompletionExpressionParser {
   
   private final ConsoleLogger logger;
   
   public CompletionExpressionParser(ConsoleLogger logger) {
      this.logger = logger;
   }

   public CompletionExpression parse(Map<String, CompletionType> types, String complete) {
      Set<String> tokens = new HashSet<String>();
      String trim = complete.trim();
      
      if(trim.endsWith(".")) {
         tokens.add(FUNCTION);
         tokens.add(CONSTANT);
         tokens.add(VARIABLE);
      } else {
         if(trim.endsWith("(")) {
            tokens.add(FUNCTION);
            tokens.add(CONSTANT);
            tokens.add(VARIABLE);
            tokens.add(MODULE);
            tokens.add(TRAIT);
            tokens.add(CLASS);
            tokens.add(ENUMERATION);
            tokens.add(TOKEN);
         } else if(complete.matches(".*new\\s+")) {
            tokens.add(CLASS);
         } else if(complete.matches(".*\\s+extends\\s+")) {
            tokens.add(TRAIT);
            tokens.add(CLASS);
         } else if(complete.matches(".*\\s+with\\s+")) {
            tokens.add(TRAIT);
         } else if(complete.matches(".*module\\s+")) {
            tokens.add(MODULE);
         }
      }
      Pattern typePattern = Pattern.compile("([a-zA-Z0-9_]+).$"); // Identifier. function  
      Matcher typeMatcher = typePattern.matcher(trim);
      CompletionType type = null;
      String constraint = null;
      
      if(typeMatcher.matches()) {
         constraint = typeMatcher.group(1);
      } else {
         Pattern functionParameterPattern = Pattern.compile("([a-zA-Z0-9_]+)\\(([a-zA-Z0-9_,\\s]+)\\).$"); // function(a,b,c).  
         Matcher functionParameterMatcher = functionParameterPattern.matcher(trim);
         
         if(functionParameterMatcher.matches()) {
            String function = functionParameterMatcher.group(1);
            String parameters = functionParameterMatcher.group(2);
            String[] parameterList = parameters.split(",");
            int count = parameterList.length;
            
            constraint = function + "(" + count + ")";
         } else {
            Pattern functionPattern = Pattern.compile("([a-zA-Z0-9_]+)\\(\\).$"); // function(a,b,c).  
            Matcher functionMatcher = functionPattern.matcher(trim);
            
            if(functionMatcher.matches()) {
               String function = functionMatcher.group(1);
               constraint = function + "(" + 0 + ")";
            }
         }
      }
      if(constraint != null) {
         type = types.get(constraint);
         
         if(type != null) {
            logger.log(complete + " is constrained to " + type);
         } else {
            logger.log(complete + " has no known constraints");
         }
      }
      return new CompletionExpression(complete, type, tokens);
   }
}
