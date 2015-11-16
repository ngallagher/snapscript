package org.snapscript.compile.instruction.define;

import static org.snapscript.core.ResultFlow.NORMAL;

import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class ClassDefinition extends Statement {   
   
   private final DefaultConstructor constructor;
   private final DefaultInitializer initializer;
   private final TypeHierarchy hierarchy;
   private final TypeName name;
   private final TypePart[] parts;
   
   public ClassDefinition(TypeName name, TypeHierarchy hierarchy, TypePart... parts) {
      this.constructor = new DefaultConstructor();
      this.initializer = new DefaultInitializer();
      this.hierarchy = hierarchy;
      this.parts = parts;
      this.name = name;
   }

   @Override
   public Result compile(Scope scope) throws Exception {
      StaticScope other = new StaticScope(scope);
      InitializerCollector collector = new InitializerCollector();
      
      // this should be passed in to the ClassHierarchy to define the type hierarchy!!!
      String n=name.evaluate(other, null).getString();
      
      Module module = other.getModule();
      Type t = module.addType(n);
      List<Type>types=t.getTypes();
     
      types.addAll(hierarchy.create(other)); // add in the type hierarchy!!

      for(TypePart part : parts) {
         Initializer s=part.define(other, collector, t);
         collector.update(s);
      }  
      initializer.execute(scope, t);
      
      List<Function>mapL=t.getFunctions();
      int count=0;
      for(Function f:mapL){
         if(f.getName().equals("new")) {
            count++;
         }
      }
      if(count==0){
         constructor.define(other, collector, t); // add the default no arg constructor!!
      }
      return NORMAL.getResult(t);
   }

}
