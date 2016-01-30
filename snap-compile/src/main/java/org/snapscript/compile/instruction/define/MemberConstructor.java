package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_CLASS;

import java.util.List;

import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Bug;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.SuperExtractor;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class MemberConstructor implements TypePart {
   
   private final MemberConstructorBuilder builder;
   private final SuperExtractor extractor;
   private final ParameterList parameters;
   private final ModifierChecker checker;
   private final TypePart part;

   public MemberConstructor(ModifierList modifiers, ParameterList parameters, Statement statement){  
      this(modifiers, parameters, null, statement);
   }  
   
   public MemberConstructor(ModifierList modifiers, ParameterList parameters, TypePart part, Statement statement){  
      this.builder = new MemberConstructorBuilder(statement);
      this.checker = new ModifierChecker(modifiers);
      this.extractor = new SuperExtractor();
      this.parameters = parameters;
      this.part = part;
   } 
   
   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception {
      return define(scope, statements, type, false);
   }
   
   @Bug("This is rubbish and needs to be cleaned up, also better way of passing enum bool")
   protected Initializer define(Scope scope, Initializer statements, Type type, boolean enumeration) throws Exception {
      Signature signature = parameters.create(scope, TYPE_CLASS);
      Initializer baseCall = extract(scope, statements, type);
      Function constructor = builder.create(signature, scope, statements, baseCall, type, enumeration);
      List<Function> functions = type.getFunctions();
      
      functions.add(constructor);
      
      return null;
   }
   
   @Bug("Its only a PrimitiveConstructor if there are no super types")
   public Initializer extract(Scope scope, Initializer statements, Type type) throws Exception {
      Type base = extractor.extractor(type);
      
      if(part != null){
         return part.define(scope, null, type);              
      }
      if(base != null) {
         return new SuperConstructor().define(scope, null, type);
      }
      return new PrimitiveConstructor(); // just create the scope object
   }
}