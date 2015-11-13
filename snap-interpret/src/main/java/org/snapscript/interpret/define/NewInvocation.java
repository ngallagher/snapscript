package org.snapscript.interpret.define;

import java.util.List;

import org.snapscript.core.Constant;
import org.snapscript.core.Initializer;
import org.snapscript.core.InstanceScope;
import org.snapscript.core.Invocation;
import org.snapscript.core.Reference;
import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.SignatureAligner;
import org.snapscript.core.Type;
import org.snapscript.interpret.ConstraintChecker;

public class NewInvocation implements Invocation<Scope> { // every constructor created must have an extra parameter 'this' for example new(x: String) is actually new(t: Type, s: String), so we pass up the parameter of type!!!
   
   private final ConstraintChecker checker;
   private final SignatureAligner aligner;
   private final Invocation constructor;
   private final Signature signature;
   private final Initializer factory;
   private final Initializer body;
   private final Type type;
   
   public NewInvocation(Type type, Signature signature, Initializer factory, Initializer body, Invocation constructor) {
      this.aligner = new SignatureAligner(signature);
      this.checker = new ConstraintChecker();
      this.constructor = constructor;
      this.signature = signature;
      this.factory = factory;
      this.body = body;
      this.type = type;
   }

   @Override
   public Result invoke(Scope scope, Scope object, Object... list) throws Exception {
      if(list.length == 0) {
         throw new IllegalArgumentException("Type '" + type + "' must be given an explicit type");
      }
      Type real = (Type)list[0];
      List<String> names = signature.getNames();
      List<Type> types = signature.getTypes();
      Object[] arguments = aligner.align(list); // combine variable arguments to a single array
      Scope inner = scope.getScope();
      
      for(int i = 0; i < arguments.length; i++) {
         Type require = types.get(i);
         String name = names.get(i);
         Object argument = arguments[i];
         
         if(!checker.compatible(scope, argument, require)) {
            throw new IllegalStateException("Parameter '" + name + "' does not match constraint '" + require + "'");
         }
         Reference reference = new Reference(argument);         
         inner.addVariable(name, reference);
      }
      // XXX HACK in the type for the new invocation e.g new(Type class, a,b,c,b)
      Result result = factory.execute(inner, real);
      Scope instance = result.getValue();
      
      if(instance == null) {
         throw new IllegalStateException("Instance could not be created");
      }
      // this could easily be the "Any" type
      //Type superT = type.getTypes().size() > 0 ? type.getTypes().get(0) : null; // XXX this is rubbish!!
      
      InstanceScope wrapper = new InstanceScope(instance, real);// we need to pass the base type up!!
      
      // Super should probably be a special variable and have special instructions!!!!!
      Constant self = new Constant(wrapper, "this");
      Constant info = new Constant(real, "class"); // give it the REAL type
      
      wrapper.addConstant("class", info);    
      wrapper.addConstant("this", self);
      //
      // functoin calls need to be handled better!!!
      // super is actually a very special value!!! its no good to just provide the base!!
      //
      /// XXX here we need to split body to fields and method
      // wrapper needs both its functions, and the super class functions in the 'this' scope???
      if(body != null) {
         body.execute(wrapper, real);
      }
      constructor.invoke(wrapper, wrapper, list);
      
      return new Result(ResultFlow.NORMAL, wrapper);
   }

}
