package org.snapscript.core;

public interface Function<T> extends Any {
   int getModifiers();
   Type getType();
   Type getDefinition();
   Type getConstraint();
   String getName();
   Signature getSignature();
   Invocation<T> getInvocation();
   String getDescription();
}
