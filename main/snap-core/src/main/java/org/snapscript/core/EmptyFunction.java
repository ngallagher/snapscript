package org.snapscript.core;

import static org.snapscript.core.ModifierType.ABSTRACT;
import static org.snapscript.core.Reserved.METHOD_CLOSURE;

public class EmptyFunction<T> implements Function<T> {

   private final FunctionDescription description;
   private final Signature signature;
   private final String name;
   private final int modifiers;

   public EmptyFunction(Signature signature){
      this(signature, METHOD_CLOSURE);
   }
   
   public EmptyFunction(Signature signature, String name){
      this(signature, name, ABSTRACT.mask);
   }
   
   public EmptyFunction(Signature signature, String name, int modifiers){
      this.description = new FunctionDescription(signature, null, name, ABSTRACT.mask);
      this.signature = signature;
      this.modifiers = modifiers;
      this.name = name;
   }
   
   public int getModifiers(){
      return modifiers;
   }
   
   public Type getType() {
      return null;
   }
   
   public Type getParent() {
      return null;
   }
   
   public String getName(){
      return name;
   }
   
   public Signature getSignature(){
      return signature;
   }
   
   public Invocation<T> getInvocation(){
      return null;
   }
   
   public String getDescription() {
      return description.getDescription();
   }
   
   @Override
   public String toString(){
      return description.toString();
   }
}
