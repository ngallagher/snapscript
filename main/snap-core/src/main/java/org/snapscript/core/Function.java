package org.snapscript.core;

public interface Function<T> {
   int getModifiers();
   Type getType();
   Type getParent();
   String getName();
   Signature getSignature();
   Invocation<T> getInvocation();
   String getDescription();
}
