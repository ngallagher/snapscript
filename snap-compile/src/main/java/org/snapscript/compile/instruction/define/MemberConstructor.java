package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_CLASS;
import static org.snapscript.core.Reserved.TYPE_CONSTRUCTOR;

import java.util.List;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.compile.instruction.StatementInvocation;
import org.snapscript.core.Bug;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Invocation;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class MemberConstructor implements TypePart {
   
   private final ParameterList parameters;
   private final ModifierList modifier;
   private final Statement statement;
   private final ArgumentList list;

   public MemberConstructor(ModifierList modifier, ParameterList parameters, Statement statement){  
      this(modifier, parameters, null, statement);
   }  
   
   public MemberConstructor(ModifierList modifier, ParameterList parameters, ArgumentList list, Statement statement){  
      this.parameters = parameters;
      this.statement = statement;
      this.modifier = modifier;
      this.list = list;
   } 
   
   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception {
      return define(scope, statements, type, false);
   }
   
   @Bug("This is rubbish and needs to be cleaned up, also better way of passing enum bool")
   protected Initializer define(Scope scope, Initializer statements, Type type, boolean enm) throws Exception {
      Signature signature = parameters.create(scope, TYPE_CLASS);
      Value mod = modifier.evaluate(scope, null);
      int modifiers = mod.getInteger();
      Initializer baseCall = extract(scope, statements, type);
 
      Invocation bodyCall = new StatementInvocation(statement, signature);   
      Invocation invocation = new NewInvocation(
            scope,
            signature,
            baseCall, // first we need to call the super constructor to create everything
            statements, // the body of the class needs to be defined next
            bodyCall,
            enm); // now call the constructor code!!!
      Invocation scopeCall = new TypeInvocation(invocation, scope);// ensure the static stuff is in scope
      Function function = new Function(signature, scopeCall, TYPE_CONSTRUCTOR, 1);// description is wrong here.....
      
      type.getFunctions().add(function);

      return null;
   }
   
   public Initializer extract(Scope scope, Initializer statements, Type type) throws Exception {
      if(list != null){
         TypePart part = new SuperConstructor(list);
         return part.define(scope, null, type);
      }
      List<Type> types = type.getTypes();
      
      for(Type base : types) {
         ArgumentList list = new ArgumentList();
         TypePart part = new SuperConstructor(list);
         return part.define(scope, null, type);                  
      }
      return new PrimitiveConstructor(); // just create the scope object
   }
}