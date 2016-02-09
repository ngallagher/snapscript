package org.snapscript.compile.instruction.define;

import static org.snapscript.core.ModifierType.CONSTANT;
import static org.snapscript.core.ModifierType.STATIC;

import org.snapscript.core.Property;
import org.snapscript.core.ScopeAccessor;
import org.snapscript.core.Type;

public class PropertyBuilder {
   
   public Property create(String name, Type type) throws Exception {
      ScopeAccessor accessor = new ScopeAccessor(name);
      return new Property(name, type, accessor, CONSTANT.mask);
   }

   public Property createStatic(String name, Type type, Object value) {
      ScopeAccessor accessor = new ScopeAccessor(name);
      return new Property(name, type, accessor, CONSTANT.mask | STATIC.mask);
   }
}
