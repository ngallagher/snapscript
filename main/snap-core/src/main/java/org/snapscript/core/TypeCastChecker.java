package org.snapscript.core;

import static org.snapscript.core.convert.ConstraintConverter.EXACT;
import static org.snapscript.core.convert.ConstraintConverter.INVALID;
import static org.snapscript.core.convert.ConstraintConverter.SIMILAR;

import java.util.List;
import java.util.Set;

import org.snapscript.core.convert.ConstraintConverter;
import org.snapscript.core.convert.ConstraintMatcher;
import org.snapscript.core.convert.FunctionComparator;

public class TypeCastChecker {

   private final FunctionComparator comparator;
   private final ClosureFunctionFinder finder;
   private final TypeExtractor extractor;
   private final TypeTraverser traverser;
   private final TypeLoader loader;
   
   public TypeCastChecker(ConstraintMatcher matcher, TypeExtractor extractor, TypeLoader loader) {
      this.comparator = new FunctionComparator(matcher);
      this.finder = new ClosureFunctionFinder(loader);
      this.traverser = new TypeTraverser();
      this.extractor = extractor;
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
               return comparator.compare((Function)value, require);
            }
         }
      }
      return cast(type, constraint);
   }
}
