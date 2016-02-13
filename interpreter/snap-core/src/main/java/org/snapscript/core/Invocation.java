package org.snapscript.core;

import org.snapscript.core.Scope;

public interface Invocation<T> {
   Result invoke(Scope scope, T object, Object... list) throws Exception;
}
