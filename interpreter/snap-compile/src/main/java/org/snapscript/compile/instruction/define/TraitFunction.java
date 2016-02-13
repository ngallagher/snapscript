package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class TraitFunction implements TypePart {

   private final TraitFunctionBuilder builder;
   private final ParameterList parameters;
   private final NameExtractor extractor;
   private final ModifierList list;
   
   public TraitFunction(ModifierList list, Evaluation identifier, ParameterList parameters) {
      this(list, identifier, parameters, null);
   }
   
   public TraitFunction(ModifierList list, Evaluation identifier, ParameterList parameters, Statement statement) {
      this.builder = new TraitFunctionBuilder(statement);
      this.extractor = new NameExtractor(identifier);
      this.parameters = parameters;
      this.list = list;
   }
   
   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception {
      List<Function> functions = type.getFunctions();  
      String name = extractor.extract(scope);
      Signature signature = parameters.create(null);
      int modifiers = list.getModifiers();
      Function function = builder.create(signature, scope, name, modifiers);
      
      functions.add(function);

      return null;
   }
   
}
