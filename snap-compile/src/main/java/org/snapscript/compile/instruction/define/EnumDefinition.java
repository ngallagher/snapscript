package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.ENUM_VALUES;
import static org.snapscript.core.Reserved.TYPE_CONSTRUCTOR;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Accessor;
import org.snapscript.core.Bug;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
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
   private final EnumList list;
   private final TypeName name;
   private final TypePart[] parts;
   
   public EnumDefinition(TypeName name, TypeHierarchy hierarcy, EnumList list, TypePart... parts) {
      this.constructor = new DefaultConstructor(true);
      this.initializer = new PropertyInitializer();
      this.hierarchy = hierarcy;
      this.parts = parts;
      this.list = list;
      this.name = name;
   }

   @Bug("This is rubbish and needs to be cleaned up")
   @Override
   public Result compile(Scope scope) throws Exception {
      StaticScope other = new StaticScope(scope);
      //InitializerCollector duh = new InitializerCollector();
      InitializerCollector collector = new InitializerCollector();

      // this should be passed in to the ClassHierarchy to define the type hierarchy!!!
      String n=name.evaluate(other, null).getString();
      
      Module module = other.getModule();
      Type t = module.addType(n);
      List<Type>types=t.getTypes();
      List values = new ArrayList();
      Value ref = ValueType.getConstant(values);
      Accessor a = new StaticAccessor(collector, other, t, ENUM_VALUES);
      Property p = new Property(ENUM_VALUES, t, a);
      t.getProperties().add(p);
      other.getState().addConstant(ENUM_VALUES, ref);
      Initializer keys = list.define(other, collector, t);
     
      types.addAll(hierarchy.create(other)); // add in the type hierarchy!!

      for(TypePart part : parts) {
         Initializer s=part.define(other, collector, t);
         collector.update(s);
      }  
      initializer.execute(scope, t);
      constructor.define(other, collector, t); // add the default no arg constructor!!

      //collector.compile(scope, t); // initialize the static scope
      keys.execute(other, t);
      collector.compile(other, t); // compile the fields after we are done!!!!
      
      return ResultType.getNormal(t);
   }


}
