package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public abstract class MemberConstructor implements TypePart {
   
   private final ConstructorAssembler assembler;
   private final MemberDeclaration list;
   private final Statement body;
   
   public MemberConstructor(MemberDeclaration list, ParameterList parameters, Statement body){  
      this(list, parameters, null, body);
   }  
   
   public MemberConstructor(MemberDeclaration list, ParameterList parameters, TypePart part, Statement body){  
      this.assembler = new ConstructorAssembler(parameters, part, body);
      this.list = list;
      this.body = body;
   } 

   protected Initializer compile(Scope scope, Initializer initializer, Type type, boolean compile) throws Exception {
      int modifiers = list.getModifiers();
      ConstructorBuilder builder = assembler.assemble(scope, initializer, type);
      Function constructor = builder.create(scope, initializer, type, modifiers, compile);
      List<Function> functions = type.getFunctions();
      
      functions.add(constructor);
      body.compile(scope);
      
      return null;
   }
}