package org.snapscript.core;

import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public interface Initializer {
   Result initialize(Scope scope, Type type) throws Exception;
}