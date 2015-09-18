package org.snapscript.interpret.define;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import org.snapscript.core.Invocation;
import org.snapscript.core.Module;
import org.snapscript.core.Reference;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class ClassInvocation implements Invocation<Scope> {

   private final Signature signature;
   private final Statement statement;
   private final String name;
   
   public ClassInvocation(Statement statement, Signature signature, String name) {
      this.statement = statement;
      this.signature = signature;
      this.name = name;
   }
   
   @Override
   public Result invoke(Scope scope, Scope object, Object... list) throws Exception {
      List<String> names = signature.getNames();
      List<Type> types = signature.getTypes();
      int modifiers = signature.getModifiers();
      
      if((modifiers & 0x00000080) != 0) {
         int length = names.size();
         int start = length - 1;
         int remaining = list.length - start;
         
         if(remaining > 0) {
            Object array = new Object[remaining];
            
            for(int i = 0; i < remaining; i++) {
               try {
                  Array.set(array, i, list[i + start]);
               } catch(Exception e){
                  throw new IllegalStateException("Invalid argument at " + i + " for" + signature, e);
               }
            }
            list[start] = array;
         }
         list = Arrays.copyOf(list, length);
      }
      // create a class scope from the passed in object!!!!!
      Module module = scope.getModule();
      Scope inner = object.getScope();
      
      for(int i = 0; i < list.length; i++) {
         Type require = types.get(i);
         String name = names.get(i);
         Object value = list[i];
         
         if(require != null) {
            Class type = value.getClass();
            Type actual = module.getType(type);
            
            if(require != actual) {
               List<Type> compatible = actual.getTypes();
               
               if(!compatible.contains(require)) {
                  throw new IllegalArgumentException("Incompatible type for " + signature);
               }
            }
         }
         Reference reference = new Reference(value);         
         inner.addVariable(name, reference);
      }
      return statement.execute(inner);
   }

}
