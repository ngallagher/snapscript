package org.snapscript.core;

public class InvocationFunction<T> implements Function<T> {

   private final FunctionDescription description;
   private final Invocation<T> invocation;
   private final Signature signature;
   private final Type definition;
   private final Type constraint;
   private final Type parent;
   private final String name;
   private final int modifiers;

   public InvocationFunction(Signature signature, Invocation<T> invocation, Type parent, Type constraint, String name){
      this(signature, invocation, parent, constraint, name, 0);
   }
   
   public InvocationFunction(Signature signature, Invocation<T> invocation, Type parent, Type constraint, String name, int modifiers){
      this(signature, invocation, parent, constraint, name, modifiers, 0);
   }
   
   public InvocationFunction(Signature signature, Invocation<T> invocation, Type parent, Type constraint, String name, int modifiers, int start){
      this.description = new FunctionDescription(signature, parent, name, start);
      this.definition = new FunctionType(signature);
      this.invocation = invocation;
      this.constraint = constraint;
      this.signature = signature;
      this.modifiers = modifiers;
      this.parent = parent;
      this.name = name;
   }
   
   public int getModifiers(){
      return modifiers;
   }
   
   public Type getType() {
      return parent;
   }
   
   public Type getDefinition() {
      return definition;
   }
   
   public Type getConstraint() {
      return constraint;
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
