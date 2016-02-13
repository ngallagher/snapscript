package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Bug;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class MemberConstructor implements TypePart {
   
   private final MemberConstructorBuilder builder;
   private final ModifierChecker checker;

   public MemberConstructor(ModifierList modifiers, ParameterList parameters, Statement statement){  
      this(modifiers, parameters, null, statement);
   }  
   
   public MemberConstructor(ModifierList modifiers, ParameterList parameters, TypePart part, Statement statement){  
      this.builder = new MemberConstructorBuilder(parameters, part, statement);
      this.checker = new ModifierChecker(modifiers);
   } 
   
   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception {
      return define(scope, statements, type, false);
   }
   
   @Bug("This is rubbish and needs to be cleaned up, also better way of passing enum bool")
   protected Initializer define(Scope scope, Initializer statements, Type type, boolean enumeration) throws Exception {
      Function constructor = builder.create(scope, statements, type, enumeration);
      List<Function> functions = type.getFunctions();
      
      functions.add(constructor);
      
      return null;
   }
}