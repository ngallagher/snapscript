package org.snapscript.compile.instruction.define;

import static org.snapscript.core.ModifierType.CONSTANT;

import org.snapscript.core.ConstantProperty;
import org.snapscript.core.Property;
import org.snapscript.core.ScopeProperty;

public class ConstantPropertyBuilder {
   
   public Property createConstant(String name) throws Exception {
      return new ScopeProperty(name, null, CONSTANT.mask);
   }

   public Property createConstant(String name, Object value) {
      return new ConstantProperty(name, null, null, value, CONSTANT.mask);
   }
}
