package org.snapscript.core;

import java.util.List;

public interface Annotation extends Any{
   List<Property> getProperties();
   String getName();
}
 