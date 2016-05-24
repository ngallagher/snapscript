package org.snapscript.core;

import java.util.List;

public interface Function<T> extends Any {
   int getModifiers();
   Type getType();
   Type getDefinition();
   Type getConstraint();
   String getName();
   Signature getSignature();
   List<Annotation> getAnnotations();
   Invocation<T> getInvocation();
   String getDescription();
}
