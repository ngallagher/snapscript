package org.snapscript.agent.debug;

import static org.snapscript.agent.debug.ScopeNode.DEPTH_KEY;
import static org.snapscript.agent.debug.ScopeNode.EXPANDABLE_KEY;
import static org.snapscript.agent.debug.ScopeNode.NAME_KEY;
import static org.snapscript.agent.debug.ScopeNode.TYPE_KEY;
import static org.snapscript.agent.debug.ScopeNode.VALUE_KEY;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.snapscript.core.InstanceScope;
import org.snapscript.core.PrimitivePromoter;
import org.snapscript.core.Type;
import org.snapscript.core.convert.ProxyExtractor;

public class ScopeNodeBuilder {
   
   private final Map<String, Map<String, String>> variables;
   private final PrimitivePromoter promoter;
   private final ScopeNodeChecker checker;
   private final ProxyExtractor extractor;
   
   public ScopeNodeBuilder(Map<String, Map<String, String>> variables) {
      this.promoter = new PrimitivePromoter();
      this.checker = new ScopeNodeChecker();
      this.extractor = new ProxyExtractor();
      this.variables = variables;
   }

   public ScopeNode createNode(String path, String name, Object object, int depth) {
      if(object != null) {
         if(object instanceof Proxy) {
            object = extractor.extract(object);
         }
         if(object instanceof InstanceScope) {
            InstanceScope instance = (InstanceScope)object;
            Type type = instance.getType();
            String text = type.getName();
            Map<String, String> criteria = createCriteria(name, "", text, true, depth);
            variables.put(path, criteria); // put the type rather than value
            return new InstanceScopeNode(this, instance, path, name, depth + 1);
         }
         Class actual = object.getClass();
         Class type = promoter.promote(actual);
         
         if(!checker.isPrimitive(type)) { 
            if(type.isArray()) {
               StringBuilder dimensions = new StringBuilder();
               Class entry = type.getComponentType();
               
               while(entry != null) {
                  dimensions.append("[]");
                  type = entry;
                  entry = type.getComponentType();
               }
               String text = type.getSimpleName();
               Map<String, String> criteria = createCriteria(name, "", text + dimensions, true, depth);
               variables.put(path, criteria); // type rather than value
               return new ArrayScopeNode(this, object, path, name, depth + 1);
            } else {
               String text = type.getSimpleName();
               Map<String, String> criteria = createCriteria(name, "", text, true, depth);
               variables.put(path, criteria); // type rather than value
               return new ObjectScopeNode(this, object, path, name, depth + 1);
            }
         } else {
            String text = type.getSimpleName();
            Map<String, String> criteria = createCriteria(name, "" + object, text, false, depth);
            variables.put(path, criteria);
         }
      }
      return null;
   }
   
   private Map<String, String> createCriteria(String name, String value, String type, boolean expandable, int depth) {
      Map<String, String> criteria = new HashMap<String, String>();
      
      criteria.put(NAME_KEY, name);
      criteria.put(TYPE_KEY, type);
      criteria.put(VALUE_KEY, value);
      criteria.put(EXPANDABLE_KEY, String.valueOf(expandable));
      criteria.put(DEPTH_KEY, String.valueOf(depth));
      
      return criteria;
   }
}