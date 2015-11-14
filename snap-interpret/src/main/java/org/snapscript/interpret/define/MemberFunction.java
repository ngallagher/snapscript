package org.snapscript.interpret.define;

import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Invocation;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.interpret.CompoundStatement;
import org.snapscript.interpret.Evaluation;
import org.snapscript.interpret.ParameterList;

public class MemberFunction implements TypePart {
   
   private final ParameterList parameters;
   private final Evaluation identifier;
   private final Statement statement;
   private final ModifierList modifier;
   
   public MemberFunction(ModifierList modifier, Evaluation identifier, ParameterList parameters, Statement statement){  
      this.identifier = identifier;
      this.parameters = parameters;
      this.statement = statement;
      this.modifier = modifier;
   } 

   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception {
      // XXX if this function is called it must be called on the internal scope of the instance...
      Value handle = identifier.evaluate(scope, null);  
      String name = handle.getString();
      Signature signature = parameters.create(scope);
      Value mod = modifier.evaluate(scope, null);
      int modifiers = mod.getInteger();
      
      if(ModifierType.isStatic(modifiers)) {
         Module module = scope.getModule();
         String qualifier=type.getName();
         //XXX invocation
         Statement init = new InitializerStatement(statements, type); // initialize static scope first
         Statement compound = new CompoundStatement(init, statement);
         Invocation invocation = new StaticInvocation(compound, signature, scope, name);
         Function functionStatic = new Function(signature, invocation, qualifier+"."+name, name);// description is wrong here.....      
         Function function = new Function(signature, invocation, name, name);// description is wrong here.....
         
         // add functions !!!!!!!!
         type.getFunctions().add(function);
         type.getFunctions().add(functionStatic);
         module.getFunctions().add(functionStatic);

         return null;//new FunctionDefinition(function,name); // we cannot invoke with scope registry
      }
      //XXX invocation
      Invocation invocation = new InstanceInvocation(statement, signature,name);
      //Invocation scopeCall = new TypeInvocation(invocation, scope); // ensure the static stuff is in scope
      Function function = new Function(signature, invocation, name, name);// description is wrong here.....
      
      // add functions !!!!!!!!
      type.getFunctions().add(function);

      return null; // functions cannot be properly invoked with just a name!!
      
   }
}