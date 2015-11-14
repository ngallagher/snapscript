package org.snapscript.core;

public class Function<T> {

   private final FunctionDescription description;
   private final Invocation<T> invocation;
   private final Signature signature;
   private final String name;

   public Function(Signature signature, Invocation<T> invocation, String name){
      this(signature, invocation, name, 0);
   }
   
   public Function(Signature signature, Invocation<T> invocation, String name, int start){
      this.description = new FunctionDescription(signature, name, start);
      this.invocation = invocation;
      this.signature = signature;
      this.name = name;
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
