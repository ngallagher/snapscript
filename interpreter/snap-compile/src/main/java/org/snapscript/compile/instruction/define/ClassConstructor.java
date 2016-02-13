package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class ClassConstructor implements TypePart {
   
   private final ConstructorAssembler assembler;
   private final ModifierChecker checker;

   public ClassConstructor(ModifierList modifiers, ParameterList parameters, Statement statement){  
      this(modifiers, parameters, null, statement);
   }  
   
   public ClassConstructor(ModifierList modifiers, ParameterList parameters, TypePart part, Statement statement){  
      this.assembler = new ConstructorAssembler(parameters, part, statement);
      this.checker = new ModifierChecker(modifiers);
   } 
   
   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception {
      return define(scope, statements, type, true);
   }
   
   protected Initializer define(Scope scope, Initializer statements, Type type, boolean compile) throws Exception {
      ConstructorBuilder builder = assembler.assemble(scope, type);
      Function constructor = builder.create(scope, statements, type, compile);
      List<Function> functions = type.getFunctions();
      
      functions.add(constructor);
      
      return null;
   }
}