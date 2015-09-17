package org.snapscript.core.define;

import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public interface TypePart {
   Statement define(Scope scope, Statement statement, Type type) throws Exception;
}
