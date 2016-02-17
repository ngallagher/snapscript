package org.snapscript.core;

import static org.snapscript.core.Reserved.SCRIPT_EXTENSION;

public class StackElementConverter {
   
   private PathConverter converter;
   private String resource;
   private int line;
   
   public StackElementConverter() {
      this.converter = new PathConverter(SCRIPT_EXTENSION);
   }
   
   public StackTraceElement create(Object value) {
      if(Function.class.isInstance(value)) {
         Function function = (Function)value;
         String func = function.getName();
         Type type = function.getType();
         
         if(type != null) {
            Module module = type.getModule();
            String prefix = module.getName();
            String name = type.getName();
            
            return new StackTraceElement(prefix + "." + name, func, resource, line);
         }
         String module = converter.convert(resource);
         return new StackTraceElement(module, func, resource, line);
      }
      if(Trace.class.isInstance(value)) {
         Trace trace = (Trace)value;
         
         resource = trace.getResource();
         line = trace.getLine();
      }
      return null;
   }
}