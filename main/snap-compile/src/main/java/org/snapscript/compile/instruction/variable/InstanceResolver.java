package org.snapscript.compile.instruction.variable;

import org.snapscript.core.Instance;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class InstanceResolver implements ValueResolver<Object> {
   
   private final ValueResolver resolver;
   
   public InstanceResolver(String name) {
      this.resolver = new LocalResolver(name);
   }
   
   @Override
   public Value resolve(Scope scope, Object left) {
      Instance instance = (Instance)scope;
      Instance outer = instance.getInstance();
      
      return resolver.resolve(outer, left);
   }
}