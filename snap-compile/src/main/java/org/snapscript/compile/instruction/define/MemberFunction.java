package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Bug;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class MemberFunction implements TypePart {
   
   private final MemberFunctionBuilder builder;
   private final ParameterList parameters;
   private final ModifierChecker checker;
   private final NameExtractor extractor;
   
   public MemberFunction(ModifierList modifiers, Evaluation identifier, ParameterList parameters, Statement statement){  
      this.builder = new MemberFunctionBuilder(statement);
      this.extractor = new NameExtractor(identifier);
      this.checker = new ModifierChecker(modifiers);
      this.parameters = parameters;
   } 

   @Bug("This is rubbish and needs to be cleaned up")
   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception {
      String name = extractor.extract(scope);
      Signature signature = parameters.create(scope);
      
      if(checker.isStatic()) {
         Module module = scope.getModule();
         Function functionStatic = builder.create(signature, statements, scope, type, name);// description is wrong here.....
         
         type.getFunctions().add(functionStatic);
         module.getFunctions().add(functionStatic); // This is VERY STRANGE!!! NEEDED BUT SHOULD NOT BE HERE!!!

         return null;//new FunctionDefinition(function,name); // we cannot invoke with scope registry
      }
      Function function = builder.create(signature, scope, type, name);// description is wrong here.....
      
      // add functions !!!!!!!!
      type.getFunctions().add(function);

      return null; // functions cannot be properly invoked with just a name!!
      
   }
}