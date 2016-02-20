package org.snapscript.core.convert;

import org.snapscript.core.HierarchyChecker;
import org.snapscript.core.Type;
import org.snapscript.core.TypeExtractor;

public class ObjectConverter extends ConstraintConverter {
   
   private final HierarchyChecker checker;
   private final TypeExtractor extractor;
   private final ProxyWrapper wrapper;
   private final Type type;
   
   public ObjectConverter(TypeExtractor extractor, ProxyWrapper wrapper, HierarchyChecker checker, Type type) {
      this.extractor = extractor;
      this.wrapper = wrapper;
      this.checker = checker;
      this.type = type;
   }

   @Override
   public int score(Object value) throws Exception {
      Type match = extractor.extract(value);
      
      if(match != null) {
         if(match.equals(type)) {
            return EXACT;
         }
         if(checker.check(match, type)) {
            return SIMILAR;
         }
         return INVALID;
      }
      return EXACT;
   }
   
   @Override
   public Object convert(Object object) {
      Class require = type.getType();
      
      if(require != null) {
         return wrapper.toProxy(object, require);
      }
      return object;
   }
}