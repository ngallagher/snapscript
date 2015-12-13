package org.snapscript.core.convert;

public class AnyConverter extends ConstraintConverter {
   
   private final ProxyBuilder builder;
   
   public AnyConverter() {
      this.builder = new ProxyBuilder();
   }
   
   @Override
   public int score(Object value) throws Exception {
      return EXACT;
   }
   
   public Object convert(Object object) {
      return builder.create(object);
   }
}
