package org.snapscript.core;

public class Function<T> {

   private final FunctionDescription description;
   private final Invocation<T> invocation;
   private final Signature signature;
   private final String name;
   private final int modifiers;

   public Function(Signature signature, Invocation<T> invocation, String name, int modifiers){
      this(signature, invocation, name, modifiers, 0);
   }
   
   public Function(Signature signature, Invocation<T> invocation, String name, int modifiers, int start){
      this.description = new FunctionDescription(signature, name, start);
      this.invocation = invocation;
      this.signature = signature;
      this.modifiers = modifiers;
      this.name = name;
   }
   
   public int getModifiers(){
      return modifiers;
   }
   
   public String getName(){
      return name;
   }
   
   public Signature getSignature(){
      return signature;
   }
   
   public Invocation<T> getInvocation(){
      return invocation;
   }
   
   @Override
   public String toString(){
      return description.toString();
   }
}
