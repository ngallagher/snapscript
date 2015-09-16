package org.snapscript.core.define;

import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.execute.Statement;

public interface TypePart {
   Statement define(Scope scope, Statement statement, Type type) throws Exception;
}
