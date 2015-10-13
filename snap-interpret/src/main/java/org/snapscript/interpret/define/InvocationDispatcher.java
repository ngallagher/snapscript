package org.snapscript.interpret.define;

import org.snapscript.core.Value;

public interface InvocationDispatcher {
   Value dispatch(String name, Object... arguments) throws Exception;
}