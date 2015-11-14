package org.snapscript.interpret.define;

import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Invocation;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.interpret.Evaluation;
import org.snapscript.interpret.ParameterList;

public class TraitFunction implements TypePart {

   private final ParameterList parameters;
   private final Evaluation identifier;
   private final Statement statement;
   private final ModifierList modifier;
   
   public TraitFunction(ModifierList modifier, Evaluation identifier, ParameterList parameters) {
      this(modifier, identifier, parameters, null);
   }
   
   public TraitFunction(ModifierList modifier, Evaluation identifier, ParameterList parameters, Statement statement) {
      this.identifier = identifier;
      this.parameters = parameters;
      this.statement = statement;
      this.modifier = modifier;
   }
   
   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception {
      // XXX if this function is called it must be called on the internal scope of the instance...
      Value handle = identifier.evaluate(null, null);  
      String name = handle.getString();
      Signature signature = parameters.create(null);
      
      //XXX invocation
      Invocation invocation = null;
      if(statement != null){
         invocation = new InstanceInvocation(statement, signature,name);
      }else {
         invocation = new InvalidInvocation(statement, signature);
      }
      Invocation scopeCall = new TypeInvocation(invocation, scope); // ensure the static stuff is in scope
      Function function = new Function(signature, scopeCall, name);// description is wrong here.....
      
      // add functions !!!!!!!!
      type.getFunctions().add(function);

      return null;
   }
   
}
