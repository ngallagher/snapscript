package org.snapscript.compile.instruction.define;

import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public interface TypePart {
   Initializer define(Scope scope, Initializer initializer, Type type) throws Exception;
}
