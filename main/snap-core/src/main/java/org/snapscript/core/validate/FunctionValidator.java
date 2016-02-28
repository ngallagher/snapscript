package org.snapscript.core.validate;

import java.util.List;
import java.util.Set;

import org.snapscript.core.Function;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Type;
import org.snapscript.core.TypeTraverser;
import org.snapscript.core.convert.ConstraintMatcher;
import org.snapscript.core.convert.FunctionComparator;

public class FunctionValidator {
   
   private final FunctionComparator comparator;
   private final TypeTraverser traverser;
   
   public FunctionValidator(ConstraintMatcher matcher, TypeTraverser traverser) {
      this.comparator = new FunctionComparator(matcher);
      this.traverser = traverser;
   }
   
   public void validate(Function function) throws Exception {
      Type type = function.getType();
      
      if(type == null) {
         throw new InternalStateException("Function '" + function + "' does not have a type");
      }
      validateModifiers(function);
   }
   
   private void validateModifiers(Function function) throws Exception {
      Type actual = function.getType();
      int modifiers = function.getModifiers();
      
      if(ModifierType.isOverride(modifiers)) {
         Set<Type> types = traverser.traverse(actual);
         int matches = 0;
         
         for(Type type : types) {
            if(type != actual) {
               List<Function> functions = type.getFunctions();
               int score = comparator.compare(function, functions);
               
               if(score != 0) {
                  matches++;
                  break;
               }
            }
         }
         if(matches == 0) {
            throw new InternalStateException("Function '" + function + "' is not an override");
         }
      }
   }
   
}
