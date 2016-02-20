package org.snapscript.core.convert;

import org.snapscript.core.Type;

public class AnyConverter extends ConstraintConverter {
   
   private final ProxyWrapper wrapper;
   
   public AnyConverter(ProxyWrapper wrapper) {
      this.wrapper = wrapper;
   }
   
   @Override
   public int score(Type type) throws Exception {
      return EXACT;
   }
   
   @Override
   public int score(Object value) throws Exception {
      return EXACT;
   }
   
   public Object convert(Object object) {
      return wrapper.toProxy(object);
   }
}
