package org.snapscript.compile.instruction.define;

import static org.snapscript.core.ModifierType.CONSTANT;
import static org.snapscript.core.ModifierType.STATIC;
import static org.snapscript.core.Reserved.TYPE_CLASS;
import static org.snapscript.core.Reserved.TYPE_THIS;

import java.util.List;

import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.core.Bug;
import org.snapscript.core.Constant;
import org.snapscript.core.ConstantAccessor;
import org.snapscript.core.Initializer;
import org.snapscript.core.Module;
import org.snapscript.core.Property;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class ClassDefinition extends Statement {   
   
   private final PropertyInitializer initializer;
   private final DefaultConstructor constructor;
   private final TypeHierarchy hierarchy;
   private final NameExtractor extractor;
   private final TypePart[] parts;
   
   public ClassDefinition(TypeName name, TypeHierarchy hierarchy, TypePart... parts) {
      this.initializer = new PropertyInitializer(TYPE_THIS);
      this.constructor = new DefaultConstructor();
      this.extractor = new NameExtractor(name);
      this.hierarchy = hierarchy;
      this.parts = parts;
   }

   @Bug("This is crap")
   @Override
   public Result compile(Scope scope) throws Exception {
      StaticScope other = new StaticScope(scope);
      InitializerCollector collector = new InitializerCollector();
      
      // this should be passed in to the ClassHierarchy to define the type hierarchy!!!
      String name=extractor.extract(scope);
      
      Module module = other.getModule();
      Type t = module.addType(name);
      List<Type>types=t.getTypes();
     
      types.addAll(hierarchy.create(other)); // add in the type hierarchy!!

      for(TypePart part : parts) {
         Initializer s=part.define(other, collector, t);
         collector.update(s);
      }  
      Type tt = module.getType(Type.class);
      Constant constant = new Constant(t);
      ConstantAccessor accessor = new ConstantAccessor(constant);
      Property property = new Property(TYPE_CLASS, tt, accessor, CONSTANT.mask | STATIC.mask);
      
      t.getProperties().add(property);
      other.getState().addConstant(TYPE_CLASS, constant);
      initializer.execute(scope, t);
      constructor.define(other, collector, t); // add the default no arg constructor!!

      //collector.compile(other, t); // do all of the static initialization!! 
      return ResultType.getNormal(t);
   }

}
