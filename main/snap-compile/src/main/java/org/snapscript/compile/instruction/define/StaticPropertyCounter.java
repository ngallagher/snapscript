package org.snapscript.compile.instruction.define;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.snapscript.core.ModifierType;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class StaticPropertyCounter {

   private final AtomicInteger counter;
   private final Type type;
   
   public StaticPropertyCounter(Type type) {
      this.counter = new AtomicInteger(-1);
      this.type = type;
   }
   
   public int count() {
      int count = counter.get();
      
      if(count == -1) {
         List<Property> properties = type.getProperties();;
         int statics = 0;
         
         for(Property property : properties) {
            int modifiers = property.getModifiers();
            
            if(ModifierType.isStatic(modifiers)) {
               statics++;
            }
         }
         counter.set(statics);
      }
      return counter.get();
   }
}
