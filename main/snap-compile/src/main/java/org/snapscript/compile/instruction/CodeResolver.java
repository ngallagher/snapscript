package org.snapscript.compile.instruction;

import java.util.HashMap;
import java.util.Map;

import org.snapscript.core.Context;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;

public class CodeResolver {
   
   private final Map<String, Code> registry;
   private final Context context;

   public CodeResolver(Context context) {
      this.registry = new HashMap<String, Code>();
      this.context = context;
   }

   public Code resolve(String name) throws Exception {
      if(registry.isEmpty()) {
         Instruction[] list = Instruction.values();       
      
         for(Instruction instruction :list){
            Code operation = create(instruction);
            String grammar = instruction.getName();
            
            registry.put(grammar, operation);
         }  
      } 
      return registry.get(name);
   }
   
   private Code create(Instruction instruction) throws Exception{
      TypeLoader loader = context.getLoader();
      Class value = instruction.getType();
      Type type = loader.loadType(value);
      
      return new Code(instruction, type);
   }
}
