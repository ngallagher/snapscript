package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.ModifierList;
import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class TypeFunction implements TypePart {
   
   private final TypeFunctionAssembler assembler;
   
   public TypeFunction(ModifierList list, Evaluation identifier, ParameterList parameters, Statement body){  
      this.assembler = new TypeFunctionAssembler(list, identifier, parameters, body);
   } 

   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception {
      TypeFunctionBuilder builder = assembler.assemble(scope, type);
      Function function = builder.create(scope, statements, type);
      List<Function> functions = type.getFunctions();
      int modifiers = function.getModifiers();

      if(ModifierType.isStatic(modifiers)) {
         Module module = scope.getModule();
         List<Function> list = module.getFunctions();
         
         list.add(function); // This is VERY STRANGE!!! NEEDED BUT SHOULD NOT BE HERE!!!
      }
      functions.add(function);

      return null; 
      
   }
}