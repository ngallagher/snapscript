package org.snapscript.compile.instruction.define;

import org.snapscript.core.Initializer;
import org.snapscript.core.Type;

public interface TypePart {
   Initializer compile(Initializer initializer, Type type) throws Exception;
}
