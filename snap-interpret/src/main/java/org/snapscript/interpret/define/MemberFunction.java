package org.snapscript.interpret.define;

import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.interpret.Evaluation;
import org.snapscript.interpret.ParameterList;
import org.snapscript.interpret.StatementInvocation;

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
   public Statement define(Scope scope, Statement statements, Type type) throws Exception {
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
         Invocation invocation = new StatementInvocation(statement, signature);
         Function functionStatic = new Function(signature, invocation, qualifier+"."+name, name);// description is wrong here.....      
         Function function = new Function(signature, invocation, name, name);// description is wrong here.....
         
         // add functions !!!!!!!!
         type.getFunctions().add(function);
         type.getFunctions().add(functionStatic);
         module.getFunctions().add(functionStatic);

         return null;//new FunctionDefinition(function,name); // we cannot invoke with scope registry
      }
      //XXX invocation
      Invocation invocation = new ClassInvocation(statement, signature,name);
      Function function = new Function(signature, invocation, name, name);// description is wrong here.....
      
      // add functions !!!!!!!!
      type.getFunctions().add(function);

      return null; // functions cannot be properly invoked with just a name!!
      
   }
}