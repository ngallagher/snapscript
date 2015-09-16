package org.snapscript.core.execute;

import org.snapscript.core.Scope;
import org.snapscript.core.Value;


public interface Evaluation{
   Value evaluate(Scope scope, Object left) throws Exception;
}