package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.AnnotationList;
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

public class MemberFunction implements TypePart {
   
   protected final MemberFunctionAssembler assembler;
   protected final Statement body;
   
   public MemberFunction(AnnotationList annotations, ModifierList modifiers, Evaluation identifier, ParameterList parameters){
      this(annotations, modifiers, identifier, parameters, null, null);
   }
   
   public MemberFunction(AnnotationList annotations, ModifierList modifiers, Evaluation identifier, ParameterList parameters, Constraint constraint){
      this(annotations, modifiers, identifier, parameters, constraint, null);
   }
   
   public MemberFunction(AnnotationList annotations, ModifierList modifiers, Evaluation identifier, ParameterList parameters, Statement body){  
      this(annotations, modifiers, identifier, parameters, null, body);
   }
   
   public MemberFunction(AnnotationList annotations, ModifierList modifiers, Evaluation identifier, ParameterList parameters, Constraint constraint, Statement body){  
      this.assembler = new MemberFunctionAssembler(annotations, modifiers, identifier, parameters, constraint, body);
      this.body = body;
   } 

   @Override
   public Initializer compile(Scope scope, Initializer initializer, Type type) throws Exception {
      return define(scope, initializer, type, 0);
   }
   
   protected Initializer define(Scope scope, Initializer initializer, Type type, int mask) throws Exception {
      MemberFunctionBuilder builder = assembler.assemble(scope, type, mask);
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