package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.*;

import java.util.List;

import org.snapscript.core.Bug;
import org.snapscript.core.Initializer;
import org.snapscript.core.Module;
import org.snapscript.core.Property;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class ClassDefinition extends Statement {   
   
   private final DefaultConstructor constructor;
   private final ClassBuilder builder;
   private final TypePart[] parts;
   
   public ClassDefinition(TypeName name, TypeHierarchy hierarchy, TypePart... parts) {
      this.builder = new ClassBuilder(name, hierarchy);
      this.constructor = new DefaultConstructor();
      this.parts = parts;
   }

   @Bug("This is crap")
   @Override
   public Result compile(Scope scope) throws Exception {
      StaticScope other = new StaticScope(scope);
      InitializerCollector collector = new InitializerCollector();
      PropertyBuilder updater = new PropertyBuilder();
      
      Type type = builder.create(other);
      Value value = ValueType.getConstant(type);
      State state = other.getState();
      
      for(TypePart part : parts) {
         Initializer initializer = part.define(other, collector, type);
         collector.update(initializer);
      } 
      Module module = scope.getModule();
      Type constraint = module.getType(Type.class);
      List<Property> properties = type.getProperties();
      Property thisProperty = updater.create(TYPE_THIS, type);
      Property superProperty = updater.create(TYPE_SUPER, type);
      Property classProperty = updater.createStatic(TYPE_CLASS, constraint, type);
      
      properties.add(classProperty);
      properties.add(superProperty);
      properties.add(thisProperty);
      state.addConstant(TYPE_CLASS, value);
      constructor.define(other, collector, type); 
      
      return ResultType.getNormal(type);
   }

}
