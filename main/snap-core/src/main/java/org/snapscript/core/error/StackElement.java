package org.snapscript.core.error;

import static org.snapscript.core.Reserved.SCRIPT_EXTENSION;

import org.snapscript.core.Function;
import org.snapscript.core.Module;
import org.snapscript.core.PathConverter;
import org.snapscript.core.Trace;
import org.snapscript.core.Type;

public class StackElement {
   
   private static final String MAIN_FUNCTION = "main";
   
   private final PathConverter converter;
   private final Function function;
   private final Trace trace;
   
   public StackElement(Trace trace) {
      this(trace, null);
   }
   
   public StackElement(Trace trace, Function function) {
      this.converter = new PathConverter(SCRIPT_EXTENSION);
      this.function = function;
      this.trace = trace;
   }
   
   public StackTraceElement build() {
      String resource = trace.getResource();
      String module = converter.convert(resource);
      int line = trace.getLine();
      
      return create(resource, module, line);
   }
   
   private StackTraceElement create(String resource, String module, int line) {
      if(function != null) {
         String name = function.getName();
         Type type = function.getType();
         
         if(type != null) {
            Module parent = type.getModule();
            String prefix = parent.getName();
            String suffix = type.getName();
            
            return new StackTraceElement(prefix + "." + suffix, name, resource, line);
         }
         return new StackTraceElement(module, name, resource, line);
      }
      return new StackTraceElement(module, MAIN_FUNCTION, resource, line);
   }
}