package org.snapscript.compile.instruction.define;

import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public interface TypeFunctionBuilder {
   Function create(Scope scope, Initializer initializer, Type type);
}
