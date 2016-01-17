package org.snapscript.engine.agent.debug;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.core.InstanceScope;
import org.snapscript.core.PrimitivePromoter;
import org.snapscript.core.Type;

public class ScopeNodeBuilder {
   
   private static final List<Class> PRIMITIVES = Arrays.<Class>asList(
         String.class,
         Integer.class,
         Double.class,
         Float.class,
         BigInteger.class,
         BigDecimal.class,
         AtomicInteger.class,
         AtomicLong.class,
         AtomicBoolean.class,
         Boolean.class,
         Short.class,
         Character.class,
         Byte.class
   );
   
   private final Map<String, String> variables;
   private final PrimitivePromoter promoter;
   
   public ScopeNodeBuilder(Map<String, String> variables) {
      this.promoter = new PrimitivePromoter();
      this.variables = variables;
   }

   public ScopeNode createNode(String path, String name, Object object) {
      if(object != null) {
         if(object instanceof InstanceScope) {
            InstanceScope instance = (InstanceScope)object;
            Type type = instance.getType();
            String text = type.getName();
            variables.put(path, text); // put the type rather than value
            return new InstanceScopeNode(this, instance, path, name);
         }
         Class actual = object.getClass();
         Class type = promoter.promote(actual);
         
         if(!PRIMITIVES.contains(type)) { 
            if(type.isArray()) {
               StringBuilder dimensions = new StringBuilder();
               Class entry = type.getComponentType();
               
               while(entry != null) {
                  dimensions.append("[]");
                  type = entry;
                  entry = type.getComponentType();
               }
               String text = type.getSimpleName();
               variables.put(path, text + dimensions); // type rather than value
               return new ArrayScopeNode(this, object, path, name);
            } else {
               String text = type.getSimpleName();
               variables.put(path, text); // type rather than value
               return new ObjectScopeNode(this, object, path, name);
            }
         } else {
            variables.put(path, "" + object);
         }
      }
      return null;
   }
}