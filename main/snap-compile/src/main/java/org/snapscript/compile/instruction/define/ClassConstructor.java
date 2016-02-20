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
   private final ModifierList list;

   public ClassConstructor(ModifierList list, ParameterList parameters, Statement statement){  
      this(list, parameters, null, statement);
   }  
   
   public ClassConstructor(ModifierList list, ParameterList parameters, TypePart part, Statement statement){  
      this.assembler = new ConstructorAssembler(parameters, part, statement);
      this.list = list;
   } 
   
   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception {
      return define(scope, statements, type, true);
   }
   
   protected Initializer define(Scope scope, Initializer statements, Type type, boolean compile) throws Exception {
      int modifiers = list.getModifiers();
      ConstructorBuilder builder = assembler.assemble(scope, type);
      Function constructor = builder.create(scope, statements, type, modifiers, compile);
      List<Function> functions = type.getFunctions();
      
      functions.add(constructor);
      
      return null;
   }
}