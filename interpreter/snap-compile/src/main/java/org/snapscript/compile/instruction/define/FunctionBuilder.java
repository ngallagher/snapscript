package org.snapscript.compile.instruction.define;

import org.snapscript.core.Bug;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

@Bug("maybe TypeFunctionBuilder?")
public interface FunctionBuilder {
   Function create(Scope scope, Initializer statements, Type type);
}
