package org.snapscript.compile.instruction;

import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.parse.Line;

public class InstructionProcessor {

   private final Context context;
   
   public InstructionProcessor(Context context) {
      this.context = context;
   }
   
   public Object process(Object value, Line line) throws Exception {
      if(Compilation.class.isInstance(value)) {
         Compilation compilation = (Compilation)value;
         String resource = line.getResource();
         int number = line.getNumber();
         
         return compilation.compile(context, resource, number);
      }
      return value;
   }
}
