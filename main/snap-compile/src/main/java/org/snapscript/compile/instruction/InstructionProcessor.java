package org.snapscript.compile.instruction;

import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleRegistry;
import org.snapscript.core.PathConverter;
import org.snapscript.parse.Line;

public class InstructionProcessor {

   private final PathConverter converter;
   private final Context context;
   
   public InstructionProcessor(Context context) {
      this.converter = new PathConverter();
      this.context = context;
   }
   
   public Object process(Object value, Line line) throws Exception {
      if(Compilation.class.isInstance(value)) {
         Compilation compilation = (Compilation)value;
         String resource = line.getResource();
         String name = converter.createModule(resource);
         ModuleRegistry registry = context.getRegistry();
         if(name==null){
            System.err.println(resource);
         }
         Module module = registry.addModule(name);
         int number = line.getNumber();
         
         return compilation.compile(module, number);
      }
      return value;
   }
}
