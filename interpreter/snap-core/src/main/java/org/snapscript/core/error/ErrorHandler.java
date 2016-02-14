package org.snapscript.core.error;

import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Trace;

public interface ErrorHandler {
   Result throwError(Scope scope, Trace trace, Result result);
   Result throwError(Scope scope, Trace trace, Throwable cause);
}
