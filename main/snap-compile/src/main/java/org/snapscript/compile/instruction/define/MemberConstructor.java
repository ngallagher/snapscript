package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.AnnotationList;
import org.snapscript.compile.instruction.ModifierList;
import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public abstract class MemberConstructor implements TypePart {
   
   private final ConstructorAssembler assembler;
   private final AnnotationList annotations;
   private final ModifierList list;
   private final Statement body;
   
   public MemberConstructor(AnnotationList annotations, ModifierList list, ParameterList parameters, Statement body){  
      this(annotations, list, parameters, null, body);
   }  
   
   public MemberConstructor(AnnotationList annotations, ModifierList list, ParameterList parameters, TypePart part, Statement body){  
      this.assembler = new ConstructorAssembler(parameters, part, body);
      this.annotations = annotations;
      this.list = list;
      this.body = body;
   } 

   protected Initializer compile(Initializer initializer, Type type, boolean compile) throws Exception {
      int modifiers = list.getModifiers();
      ConstructorBuilder builder = assembler.assemble(initializer, type);
      Function constructor = builder.create(initializer, type, modifiers, compile);
      List<Function> functions = type.getFunctions();
      Scope scope = type.getScope();
      
      annotations.apply(scope, constructor);
      functions.add(constructor);
      body.compile(scope);
      
      return null;
   }
}