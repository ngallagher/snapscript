package org.snapscript.core;

public class Function<T> {

   private final FunctionDescription description;
   private final Invocation<T> invocation;
   private final Signature signature;
   private final String name;
   private final Type type;
   private final int modifiers;

   public Function(Signature signature, Invocation<T> invocation, Type type, String name, int modifiers){
      this(signature, invocation, type, name, modifiers, 0);
   }
   
   public Function(Signature signature, Invocation<T> invocation, Type type, String name, int modifiers, int start){
      this.description = new FunctionDescription(signature, type, name, start);
      this.invocation = invocation;
      this.signature = signature;
      this.modifiers = modifiers;
      this.name = name;
      this.type = type;
   }
   
   public int getModifiers(){
      return modifiers;
   }
   
   public Type getType() {
      return type;
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
   
   public String getDescription() {
      return description.getDescription();
   }
   
   @Override
   public String toString(){
      return description.toString();
   }
}
