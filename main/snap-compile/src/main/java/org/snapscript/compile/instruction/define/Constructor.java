package org.snapscript.compile.instruction.define;

import org.snapscript.core.Instance;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;

public interface Constructor {
   Result invoke(Scope scope, Instance object, Object... list) throws Exception;
}
