package org.snapscript.compile.instruction.define;

import static org.snapscript.core.ModifierType.PUBLIC;
import static org.snapscript.core.Reserved.ANY_TYPE;
import static org.snapscript.core.Reserved.DEFAULT_PACKAGE;
import static org.snapscript.core.Reserved.METHOD_ARGUMENT;
import static org.snapscript.core.Reserved.METHOD_EQUALS;
import static org.snapscript.core.Reserved.METHOD_HASH_CODE;
import static org.snapscript.core.Reserved.METHOD_TO_STRING;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Context;
import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.InvocationFunction;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;

public class AnyDefinition extends Statement {
   
   private final DefaultConstructor constructor;
   
   public AnyDefinition(){
      this.constructor = new DefaultConstructor();
   }

   @Override
   public Result compile(Scope scope) throws Exception {
      Module module = scope.getModule();
      Context context = module.getContext();
      TypeLoader loader = context.getLoader();
      Type type = loader.defineType(DEFAULT_PACKAGE, ANY_TYPE);
      List<Function> functions = type.getFunctions();
      
      if(functions.isEmpty()) {
         Scope value = module.getScope();
         Function hashCode = createHashCode(type);
         Function toString = createToString(type);
         Function equals = createEquals(type);
         
         functions.add(hashCode);
         functions.add(equals);
         functions.add(toString);
         constructor.define(value, null, type);
      }
      return ResultType.getNormal(type);
   }
   
   private Function createHashCode(Type type) {
      List<Type> types = new ArrayList<Type>();
      List<String> names = new ArrayList<String>();
      Signature signature = new Signature(names, types);
      Invocation<Object> invocation = new HashCodeInvocation();
      
      return new InvocationFunction<Object>(signature, invocation, type, METHOD_HASH_CODE, PUBLIC.mask);
   }
   
   private Function createEquals(Type type) {
      List<Type> types = new ArrayList<Type>();
      List<String> names = new ArrayList<String>();
      Signature signature = new Signature(names, types);
      Invocation<Object> invocation = new EqualsInvocation();
      
      types.add(null);
      names.add(METHOD_ARGUMENT);
      
      return new InvocationFunction<Object>(signature, invocation, type, METHOD_EQUALS, PUBLIC.mask);
   }
   
   private Function createToString(Type type) {
      List<Type> types = new ArrayList<Type>();
      List<String> names = new ArrayList<String>();
      Signature signature = new Signature(names, types);
      Invocation<Object> invocation = new ToStringInvocation();
      
      return new InvocationFunction<Object>(signature, invocation, type, METHOD_TO_STRING, PUBLIC.mask);
   }
   
   private static class HashCodeInvocation implements Invocation<Object> {
      
      @Override
      public Result invoke(Scope scope, Object object, Object... list) throws Exception {
         int hash = object.hashCode();
         return ResultType.getNormal(hash);
      }
   }
   
   private static class EqualsInvocation implements Invocation<Object> {
      
      @Override
      public Result invoke(Scope scope, Object object, Object... list) throws Exception {
         Object argument = list[0];
         boolean equal = object.equals(argument);
         
         return ResultType.getNormal(equal);
      }
   }
   
   private static class ToStringInvocation implements Invocation<Object> {
      
      @Override
      public Result invoke(Scope scope, Object object, Object... list) throws Exception {
         String value = object.toString();
         int hash = object.hashCode();
         
         return ResultType.getNormal(value + "@" + hash);
      }
   }
}
