package org.snapscript.compile.define;

import org.snapscript.core.Type;
import org.snapscript.core.define.Initializer;

public interface TypePart {
   Initializer compile(Initializer initializer, Type type) throws Exception;
}
