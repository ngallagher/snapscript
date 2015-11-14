package org.snapscript.core;
import java.util.List;

public class FunctionDescription {

   private final Signature signature;
   private final String name;
   private final int start;
  
   public FunctionDescription(Signature signature, String name, int start) {
      this.signature = signature;
      this.start = start;
      this.name = name;
   }
   
   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      
      builder.append(name);
      builder.append("(");
      
      List<Type> types = signature.getTypes();
      List<String> names = signature.getNames();
      int size = names.size();
      
      for(int i = start; i < size; i++) {
         Type type = types.get(i);
         String name = names.get(i);
         
         if(i > 0) {
            builder.append(", ");
         }
         builder.append(name);
         
         if(type != null) {
            String constraint = type.getName();
            
            builder.append(": ");
            builder.append(constraint);
         }
      }
      builder.append(")");
      return builder.toString();
   }
}
