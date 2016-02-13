package org.snapscript.core.convert;

public class AnyConverter extends ConstraintConverter {
   
   private final ProxyWrapper wrapper;
   
   public AnyConverter() {
      this.wrapper = new ProxyWrapper();
   }
   
   @Override
   public int score(Object value) throws Exception {
      return EXACT;
   }
   
   public Object convert(Object object) {
      return wrapper.toProxy(object);
   }
}
