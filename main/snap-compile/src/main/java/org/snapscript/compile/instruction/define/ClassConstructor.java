package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.ModifierList;
import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class ClassConstructor implements TypePart {
   
   private final ConstructorAssembler assembler;
   private final ModifierList list;
   private final Statement body;
   
   public ClassConstructor(ModifierList list, ParameterList parameters, Statement body){  
      this(list, parameters, null, body);
   }  
   
   public ClassConstructor(ModifierList list, ParameterList parameters, TypePart part, Statement body){  
      this.assembler = new ConstructorAssembler(parameters, part, body);
      this.list = list;
      this.body = body;
   } 
   
   @Override
   public Initializer define(Scope scope, Initializer initializer, Type type) throws Exception {
      return define(scope, initializer, type, true);
   }
   
   protected Initializer define(Scope scope, Initializer initializer, Type type, boolean compile) throws Exception {
      int modifiers = list.getModifiers();
      ConstructorBuilder builder = assembler.assemble(scope, initializer, type);
      Function constructor = builder.create(scope, initializer, type, modifiers, compile);
      List<Function> functions = type.getFunctions();
      
      functions.add(constructor);
      body.compile(scope);
      
      return null;
   }
}