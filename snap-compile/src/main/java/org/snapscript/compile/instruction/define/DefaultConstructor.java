package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_CONSTRUCTOR;

import java.util.List;

import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.NoStatement;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class DefaultConstructor implements TypePart {
   
   private final ParameterList parameters;
   private final ModifierList modifiers;
   private final boolean enumeration;

   public DefaultConstructor(){
      this(false);
   }
   
   public DefaultConstructor(boolean enumeration) {
      this.parameters = new ParameterList();
      this.modifiers = new ModifierList();
      this.enumeration = enumeration;
   } 
   
   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception {
      List<Function> functions = type.getFunctions();
      
      for(Function function : functions) {
         String name = function.getName();
         
         if(name.equals(TYPE_CONSTRUCTOR)) {
            return null;
         }
      }
      return define(scope, statements, type, enumeration);
   }
   
   protected Initializer define(Scope scope, Initializer statements, Type type, boolean enumeration) throws Exception {
      Statement statement = new NoStatement();
      MemberConstructor constructor = new MemberConstructor(modifiers, parameters, statement);
      
      return constructor.define(scope, statements, type, enumeration);
   }
}