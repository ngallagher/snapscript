package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.Constraint;
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
   
   protected final TypeFunctionAssembler assembler;
   protected final Statement body;
   
   public TypeFunction(ModifierList list, Evaluation identifier, ParameterList parameters){
      this(list, identifier, parameters, null, null);
   }
   
   public TypeFunction(ModifierList list, Evaluation identifier, ParameterList parameters, Constraint constraint){
      this(list, identifier, parameters, constraint, null);
   }
   
   public TypeFunction(ModifierList list, Evaluation identifier, ParameterList parameters, Statement body){  
      this(list, identifier, parameters, null, body);
   }
   
   public TypeFunction(ModifierList list, Evaluation identifier, ParameterList parameters, Constraint constraint, Statement body){  
      this.assembler = new TypeFunctionAssembler(list, identifier, parameters, constraint, body);
      this.body = body;
   } 

   @Override
   public Initializer compile(Scope scope, Initializer initializer, Type type) throws Exception {
      return define(scope, initializer, type, 0);
   }
   
   protected Initializer define(Scope scope, Initializer initializer, Type type, int mask) throws Exception {
      TypeFunctionBuilder builder = assembler.assemble(scope, type, mask);
      Function function = builder.create(scope, initializer, type);
      List<Function> functions = type.getFunctions();
      int modifiers = function.getModifiers();

      if(ModifierType.isStatic(modifiers)) {
         Module module = scope.getModule();
         List<Function> list = module.getFunctions();
         
         list.add(function); // This is VERY STRANGE!!! NEEDED BUT SHOULD NOT BE HERE!!!
      }
      functions.add(function);
      
      if(body != null) {
         body.compile(scope);
      }
      return null; 
   }
}