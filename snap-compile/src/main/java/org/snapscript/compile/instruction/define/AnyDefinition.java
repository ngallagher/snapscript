package org.snapscript.compile.instruction.define;

import static org.snapscript.core.ResultFlow.NORMAL;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Context;
import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
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
      Type type = loader.defineType("Any", null);
      List<Function> functions = type.getFunctions();
      
      if(functions.isEmpty()) {
         Function hashCode = createHashCode();
         Function toString = createToString();
         Function equals = createEquals();
         
         functions.add(hashCode);
         functions.add(equals);
         functions.add(toString);
         constructor.define(scope, null, type);
      }
      return NORMAL.getResult(type);
   }
   
   private Function createHashCode() {
      List<Type> types = new ArrayList<Type>();
      List<String> names = new ArrayList<String>();
      Signature signature = new Signature(names, types, 1);
      Invocation<Object> invocation = new HashCodeInvocation();
      
      return new Function<Object>(signature, invocation, "hashCode");
   }
   
   private Function createEquals() {
      List<Type> types = new ArrayList<Type>();
      List<String> names = new ArrayList<String>();
      Signature signature = new Signature(names, types, 1);
      Invocation<Object> invocation = new EqualsInvocation();
      
      types.add(null);
      names.add("object");
      
      return new Function<Object>(signature, invocation, "equals");
   }
   
   private Function createToString() {
      List<Type> types = new ArrayList<Type>();
      List<String> names = new ArrayList<String>();
      Signature signature = new Signature(names, types, 1);
      Invocation<Object> invocation = new ToStringInvocation();
      
      return new Function<Object>(signature, invocation, "toString");
   }
   
   private static class HashCodeInvocation implements Invocation<Object> {
      
      @Override
      public Result invoke(Scope scope, Object object, Object... list) throws Exception {
         int hash = object.hashCode();
         return NORMAL.getResult(hash);
      }
   }
   
   private static class EqualsInvocation implements Invocation<Object> {
      
      @Override
      public Result invoke(Scope scope, Object object, Object... list) throws Exception {
         boolean equal = object.equals(list[0]);
         return NORMAL.getResult(equal);
      }
   }
   
   private static class ToStringInvocation implements Invocation<Object> {
      
      @Override
      public Result invoke(Scope scope, Object object, Object... list) throws Exception {
         String value = object.toString();
         int hash = object.hashCode();
         
         return NORMAL.getResult(value + "@" + hash);
      }
   }
}
