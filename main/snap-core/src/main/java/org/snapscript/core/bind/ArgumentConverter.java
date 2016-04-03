package org.snapscript.core.bind;

import org.snapscript.core.convert.Score;

public interface ArgumentConverter { 
   Score score(Object... list) throws Exception;
   Object[] convert(Object... list) throws Exception;
}
