package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class ClassConstructor extends MemberConstructor {
   
   public ClassConstructor(MemberDeclaration list, ParameterList parameters, Statement body){  
      super(list, parameters, null, body);
   }  
   
   public ClassConstructor(MemberDeclaration list, ParameterList parameters, TypePart part, Statement body){  
      super(list, parameters, part, body);
   } 
   
   @Override
   public Initializer compile(Scope scope, Initializer initializer, Type type) throws Exception {
      return compile(scope, initializer, type, true);
   }
}