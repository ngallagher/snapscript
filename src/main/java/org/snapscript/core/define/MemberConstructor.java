package org.snapscript.core.define;

import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.execute.ArgumentList;
import org.snapscript.core.execute.ParameterList;
import org.snapscript.core.execute.Statement;
import org.snapscript.core.execute.StatementInvocation;


// blah(a,b,c):super(a,b);

public class MemberConstructor implements TypePart {
   
   private final ParameterList parameters;
   private final Statement statement;
   private final ArgumentList list;
   private final ModifierList modifier;

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
   public Statement define(Scope scope, Statement statements, Type type) throws Exception {
      Signature signature = parameters.create(scope);
      Value mod = modifier.evaluate(scope, null);
      int modifiers = mod.getInteger();
      Statement baseCall = null;
      
      if(list != null){
         baseCall = new SuperConstructor(list).define(scope, statements, type);
      }else {
         
         List<Type> types=type.getTypes();
            Type superT=types.isEmpty()?null:types.get(0);
         if(superT!=null) {
            // here we call the anon constructor
            baseCall = new SuperConstructor(new ArgumentList()).define(scope, statements, type);                  
         } else {
            baseCall = new PrimitiveConstructor(); // just create the scope object
         }
      }
      Invocation bodyCall = new StatementInvocation(statement, signature);      
      Invocation invocation = new NewInvocation(
            type,
            signature,
            baseCall, // first we need to call the super constructor to create everything
            statements, // the body of the class needs to be defined next
            bodyCall); // now call the constructor code!!!
      
      //
      // this function does the following in order...
      //
      // 1) delegate to super constructor if declared
      // 2) if no super is declared it calls the no argument constructor
      // 3) if no super type is defined via "extends" it creates a default constructor
      // 4) After invoking the super constructor the result will be a scope object
      //
      Function function = new Function(signature, invocation, "new", "new");// description is wrong here.....
      
      // add functions !!!!!!!!
      type.getFunctions().add(function);
      
      //FunctionDeclaration declaration = new FunctionDeclaration(identifier, parameters, fields);

      //declaration.execute(scope); // register constructor which will call fields and constructor
      
      //Function function = scope.getFunction("new"); // this is the new function that was declared...
      //XXX where do we put the function????
      return null;
   }
}