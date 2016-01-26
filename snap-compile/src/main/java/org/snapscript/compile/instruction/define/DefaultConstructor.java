package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Bug;
import org.snapscript.core.Initializer;
import org.snapscript.core.NoStatement;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

@Bug("This is rubbish and needs to be cleaned up")
public class DefaultConstructor implements TypePart {
   
   private final ParameterList parameters;
   private final ModifierList modifiers;
   private final boolean enm;

   public DefaultConstructor(){
      this(false);
   }
   
   public DefaultConstructor(boolean enm) {
      this.parameters = new ParameterList();
      this.modifiers = new ModifierList();
      this.enm = enm;
   } 
   
   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception {
      return define(scope, statements, type, enm);
   }
   
   protected Initializer define(Scope scope, Initializer statements, Type type, boolean enm) throws Exception {
      Statement statement = new NoStatement();
      MemberConstructor constructor = new MemberConstructor(modifiers, parameters, statement);
      
      return constructor.define(scope, statements, type, enm);
   }
}