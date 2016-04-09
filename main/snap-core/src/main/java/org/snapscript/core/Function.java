package org.snapscript.core;

public interface Function<T> {
   int getModifiers();
   Type getType();
   Type getDefinition();
   Type getConstraint();
   String getName();
   Signature getSignature();
   Invocation<T> getInvocation();
   String getDescription();
}
