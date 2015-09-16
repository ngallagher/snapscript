package org.snapscript.core.execute;

import org.snapscript.core.Scope;

public interface Library {
   void include(Scope scope) throws Exception;
}
