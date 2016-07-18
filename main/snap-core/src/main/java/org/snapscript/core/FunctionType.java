package org.snapscript.core;

import static org.snapscript.core.Reserved.METHOD_CLOSURE;

import java.util.Collections;
import java.util.List;

public class FunctionType implements Type {
   
   private final Function function;
   private final Module module;
   
   public FunctionType(Signature signature, Module module) {
      this.function = new EmptyFunction(signature, METHOD_CLOSURE);
      this.module = module;
   }
   
   @Override
   public List<Annotation> getAnnotations() {
      return Collections.emptyList();
   }

   @Override
   public List<Property> getProperties() {
      return Collections.emptyList();
   }

   @Override
   public List<Function> getFunctions() {
      return Collections.singletonList(function);
   }

   @Override
   public List<Type> getTypes() {
      return Collections.emptyList();
   }

   @Override
   public Module getModule() {
      return module;
   }

   @Override
   public Class getType() {
      return null;
   }

   @Override
   public Type getEntry() {
      return null;
   }

   @Override
   public String getName() {
      return null;
   }
   
   @Override
   public int getOrder() {
      return 0;
   }

}
