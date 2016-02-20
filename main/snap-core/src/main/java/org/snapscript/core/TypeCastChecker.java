package org.snapscript.core;

import static org.snapscript.core.convert.ConstraintConverter.EXACT;
import static org.snapscript.core.convert.ConstraintConverter.INVALID;
import static org.snapscript.core.convert.ConstraintConverter.SIMILAR;

import java.util.List;
import java.util.Set;

import org.snapscript.core.convert.ConstraintConverter;
import org.snapscript.core.convert.ConstraintMatcher;

public class TypeCastChecker {

   private final ClosureFunctionFinder finder;
   private final ConstraintMatcher matcher;
   private final TypeExtractor extractor;
   private final TypeTraverser traverser;
   private final TypeLoader loader;
   
   public TypeCastChecker(ConstraintMatcher matcher, TypeExtractor extractor, TypeLoader loader) {
      this.finder = new ClosureFunctionFinder(loader);
      this.traverser = new TypeTraverser();
      this.extractor = extractor;
      this.matcher = matcher;
      this.loader = loader;
   }
   
   public int cast(Class actual, Type constraint) throws Exception {
      Type type = loader.loadType(actual);

      if(!actual.equals(constraint)) {
         return cast(type, constraint);
      }
      return EXACT;
   }
   
   public int cast(Type actual, Type constraint) throws Exception {
      if(!actual.equals(constraint)) {
         Set<Type> list = traverser.traverse(actual);
         
         if(list.isEmpty()) {
            return INVALID;
         }
         if(list.contains(constraint)) {
            return SIMILAR;
         }
         return INVALID;
      }
      return EXACT;
   }
   
   public int cast(Object value, Type constraint) throws Exception {
      Type type = extractor.extract(value);
      
      if(Function.class.isInstance(value)) {
         Class real = constraint.getType();
         
         if(real != null) {
            Function require = finder.find(real);
            
            if(require != null) {
               return cast((Function)value, require);
            }
         }
      }
      return cast(type, constraint);
   }
   
   private int cast(Function actual, Function require) throws Exception{
      Signature actualSignature = actual.getSignature();
      Signature requireSignature = require.getSignature();
      List<Type> actualTypes = actualSignature.getTypes();
      List<Type> constraintTypes = requireSignature.getTypes();
      
      return cast(actualTypes, constraintTypes);
   }
   
   private int cast(List<Type> actual, List<Type> require) throws Exception{
      int actualSize = actual.size();
      int requireSize = require.size();
      
      if(actualSize == requireSize) {
         int minimum = EXACT;
         
         for(int i = 0; i < actualSize; i++) {
            Type actualType = actual.get(i);
            Type constraintType = require.get(i);
            ConstraintConverter converter = matcher.match(constraintType);
            int score = converter.score(actualType);
            
            if(score <= INVALID) { // must check for numbers
               return INVALID;
            }
            if(score < minimum) {
               minimum = score;
            }
         }
         return minimum;
      }
      return INVALID;
   }
}
