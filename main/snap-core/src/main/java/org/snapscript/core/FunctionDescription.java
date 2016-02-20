package org.snapscript.core;

import java.util.List;

public class FunctionDescription {

   private final Signature signature;
   private final String name;
   private final Type type;
   private final int start;
  
   public FunctionDescription(Signature signature, Type type, String name, int start) {
      this.signature = signature;
      this.start = start;
      this.name = name;
      this.type = type;
   }
   
   public String getDescription() {
      StringBuilder builder = new StringBuilder();
      
      if(type != null) {
         String name = type.getName();
         Module module = type.getModule();
         String prefix = module.getName();
         
         builder.append(prefix);
         builder.append(".");
         builder.append(name);
         builder.append(".");
      } 
      builder.append(name);
      return builder.toString();
   }
   
   public String getParameters() {
      StringBuilder builder = new StringBuilder();

      builder.append("(");
      
      if(signature != null) {
         List<Type> types = signature.getTypes();
         List<String> names = signature.getNames();
         int size = names.size();
         
         for(int i = start; i < size; i++) {
            Type type = types.get(i);
            String name = names.get(i);
            
            if(i > start) {
               builder.append(", ");
            }
            builder.append(name);
            
            if(type != null) {
               String constraint = type.getName();
               
               builder.append(": ");
               builder.append(constraint);
            }
         }
      }
      builder.append(")");
      return builder.toString();
   }
   
   @Override
   public String toString() {
      String description = getDescription();
      String parameters = getParameters();
      
      return description + parameters;
   }
}
