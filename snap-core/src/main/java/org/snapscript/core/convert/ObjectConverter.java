package org.snapscript.core.convert;

import org.snapscript.core.InstanceChecker;
import org.snapscript.core.Type;
import org.snapscript.core.TypeExtractor;

public class ObjectConverter extends ConstraintConverter {
   
   private final TypeExtractor extractor;
   private final InstanceChecker checker;
   private final ProxyBuilder builder;
   private final Type type;
   
   public ObjectConverter(TypeExtractor extractor, InstanceChecker checker, Type type) {
      this.builder = new ProxyBuilder();
      this.extractor = extractor;
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
         return builder.create(object, require);
      }
      return object;
   }
}