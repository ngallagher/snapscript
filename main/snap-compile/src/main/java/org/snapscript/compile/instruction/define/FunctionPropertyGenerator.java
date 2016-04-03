package org.snapscript.compile.instruction.define;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.snapscript.core.AccessorProperty;
import org.snapscript.core.Function;
import org.snapscript.core.Property;
import org.snapscript.core.PropertyNameExtractor;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;

public class FunctionPropertyGenerator {

   private static final String[] PREFIXES = {"get", "is"};
   
   private final Set<String> done;
   
   public FunctionPropertyGenerator() {
      this.done = new HashSet<String>();
   }
   
   public void generate(Type type) throws Exception {
      List<Function> functions = type.getFunctions();
      List<Property> properties = type.getProperties();
      
      for(Property property : properties) {
         String name = property.getName();
         
         if(name != null) {
            done.add(name);
         }
      }
      for(Function function : functions) {
         Signature signature = function.getSignature();
         List<String> names = signature.getNames();
         int modifiers = function.getModifiers();
         int count = names.size();
         
         if(count == 0) {
            String name = extract(function);
   
            if(done.add(name)) {
               FunctionAccessor accessor = new FunctionAccessor(function);
               AccessorProperty property = new AccessorProperty(name, type, accessor, modifiers);
         
               properties.add(property);
            }
         }
      }
   }
   
   private String extract(Function function) throws Exception {
      String name = function.getName();
      
      for(String prefix : PREFIXES) {
         String property = PropertyNameExtractor.getProperty(name, prefix);
      
         if(property != null) {
            return property;
         }
      }
      return name;
   }
}
