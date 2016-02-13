package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.ENUM_VALUES;
import static org.snapscript.core.Reserved.TYPE_CLASS;
import static org.snapscript.core.Reserved.TYPE_SUPER;
import static org.snapscript.core.Reserved.TYPE_THIS;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.compile.instruction.NameExtractor;
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

public class EnumDefinition extends Statement {

   private final DefaultConstructor constructor;
   private final TypeHierarchy hierarchy;
   private final NameExtractor extractor;
   private final EnumList list;
   private final TypePart[] parts;
   
   public EnumDefinition(TypeName name, TypeHierarchy hierarcy, EnumList list, TypePart... parts) {
      this.constructor = new DefaultConstructor(true);
      this.extractor = new NameExtractor(name);
      this.hierarchy = hierarcy;
      this.parts = parts;
      this.list = list;
   }

   @Bug("This is rubbish and needs to be cleaned up")
   @Override
   public Result compile(Scope scope) throws Exception {
      StaticScope other = new StaticScope(scope);
      InitializerCollector collector = new InitializerCollector();
      PropertyBuilder updater = new PropertyBuilder();
      List values = new ArrayList();
      
      String name = extractor.extract(scope);
      Module module = other.getModule();
      Type type = module.addType(name);
      Initializer keys = list.define(other, collector, type);
     
      hierarchy.update(other, type); // add in the type hierarchy!!

      for(TypePart part : parts) {
         Initializer initializer = part.define(other, collector, type);
         collector.update(initializer);
      }  
      State state = other.getState();
      Value classValue = ValueType.getConstant(type);
      Value enumValues = ValueType.getConstant(values);
      Type typeConstraint = module.getType(Type.class);
      Type listConstraint = module.getType(List.class);
      List<Property> properties = type.getProperties();
      Property thisProperty = updater.create(TYPE_THIS, type);
      Property superProperty = updater.create(TYPE_SUPER, type);
      Property classProperty = updater.createStatic(TYPE_CLASS, typeConstraint, type); // do we really need a constraint??
      Property valuesProperty = updater.createStatic(ENUM_VALUES, listConstraint, values);
      
      properties.add(classProperty);
      properties.add(superProperty);
      properties.add(thisProperty);
      properties.add(valuesProperty);
      state.addConstant(ENUM_VALUES, enumValues);
      state.addConstant(TYPE_CLASS, classValue);
      constructor.define(other, collector, type); // add the default no arg constructor!!

      //collector.compile(scope, t); // initialize the static scope
      keys.execute(other, type);
      collector.compile(other, type); // compile the fields after we are done!!!!
      
      return ResultType.getNormal(type);
   }


}
