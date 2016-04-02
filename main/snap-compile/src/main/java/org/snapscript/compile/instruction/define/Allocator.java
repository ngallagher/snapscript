package org.snapscript.compile.instruction.define;

import org.snapscript.core.Scope;
import org.snapscript.core.instance.Instance;

public interface Allocator {
   Instance allocate(Scope scope, Instance base, Object... list) throws Exception;
}
