package org.snapscript.compile.instruction.define;

import org.snapscript.core.Instance;
import org.snapscript.core.Scope;

public interface Allocator {
   Instance allocate(Scope scope, Instance base, Object... list) throws Exception;
}
