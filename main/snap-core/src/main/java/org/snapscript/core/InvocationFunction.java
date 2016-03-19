package org.snapscript.core;

public class InvocationFunction<T> implements Function<T> {

   private final FunctionDescription description;
   private final Invocation<T> invocation;
   private final Signature signature;
   private final String name;
   private final Type parent;
   private final Type type;
   private final int modifiers;

   public InvocationFunction(Signature signature, Invocation<T> invocation, Type parent, String name){
      this(signature, invocation, parent, name, 0);
   }
   
   public InvocationFunction(Signature signature, Invocation<T> invocation, Type parent, String name, int modifiers){
      this(signature, invocation, parent, name, modifiers, 0);
   }
   
   public InvocationFunction(Signature signature, Invocation<T> invocation, Type parent, String name, int modifiers, int start){
      this.description = new FunctionDescription(signature, parent, name, start);
      this.type = new FunctionType(signature);
      this.invocation = invocation;
      this.signature = signature;
      this.modifiers = modifiers;
      this.parent = parent;
      this.name = name;
   }
   
   public int getModifiers(){
      return modifiers;
   }
   
   public Type getType() {
      return type;
   }
   
   public Type getParent() {
      return parent;
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
