package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.ENUM_VALUES;
import static org.snapscript.core.ModifierType.*;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.core.Accessor;
import org.snapscript.core.Bug;
import org.snapscript.core.Initializer;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Module;
import org.snapscript.core.Property;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.StaticAccessor;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class EnumDefinition extends Statement {

   private final DefaultConstructor constructor;
   private final PropertyInitializer initializer;
   private final TypeHierarchy hierarchy;
   private final NameExtractor extractor;
   private final EnumList list;
   private final TypePart[] parts;
   
   public EnumDefinition(TypeName name, TypeHierarchy hierarcy, EnumList list, TypePart... parts) {
      this.constructor = new DefaultConstructor(true);
      this.initializer = new PropertyInitializer();
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

      // this should be passed in to the ClassHierarchy to define the type hierarchy!!!
      String name = extractor.extract(scope);
      
      Module module = other.getModule();
      Type type = module.addType(name);
      List<Type>types=type.getTypes();
      List values = new ArrayList();
      Value ref = ValueType.getConstant(values);
      Accessor accessor = new StaticAccessor(collector, other, type, ENUM_VALUES);
      Property property = new Property(ENUM_VALUES, type, accessor, CONSTANT.mask | STATIC.mask);
      type.getProperties().add(property);
      other.getState().addConstant(ENUM_VALUES, ref);
      Initializer keys = list.define(other, collector, type);
     
      types.addAll(hierarchy.create(other)); // add in the type hierarchy!!

      for(TypePart part : parts) {
         Initializer s=part.define(other, collector, type);
         collector.update(s);
      }  
      initializer.execute(scope, type);
      constructor.define(other, collector, type); // add the default no arg constructor!!

      //collector.compile(scope, t); // initialize the static scope
      keys.execute(other, type);
      collector.compile(other, type); // compile the fields after we are done!!!!
      
      return ResultType.getNormal(type);
   }


}
