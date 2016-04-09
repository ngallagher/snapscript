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
   
   @Override
   public int getModifiers(){
      return modifiers;
   }
   
   @Override
   public Type getDefinition() {
      return null;
   }
   
   @Override
   public Type getConstraint() {
      return null;
   }
   
   @Override
   public Type getType() {
      return null;
   }
   
   @Override
   public String getName(){
      return name;
   }
   
   @Override
   public Signature getSignature(){
      return signature;
   }
   
   @Override
   public Invocation<T> getInvocation(){
      return null;
   }
   
   @Override
   public String getDescription() {
      return description.getDescription();
   }
   
   @Override
   public String toString(){
      return description.toString();
   }
}
