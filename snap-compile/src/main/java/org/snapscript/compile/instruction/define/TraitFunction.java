package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class TraitFunction implements TypePart {

   private final TraitFunctionBuilder builder;
   private final ParameterList parameters;
   private final ModifierList modifiers;
   private final Evaluation identifier;
   private final Statement statement;
   
   public TraitFunction(ModifierList modifiers, Evaluation identifier, ParameterList parameters) {
      this(modifiers, identifier, parameters, null);
   }
   
   public TraitFunction(ModifierList modifiers, Evaluation identifier, ParameterList parameters, Statement statement) {
      this.builder = new TraitFunctionBuilder(statement);
      this.identifier = identifier;
      this.parameters = parameters;
      this.statement = statement;
      this.modifiers = modifiers;
   }
   
   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception {
      List<Function> functions = type.getFunctions();
      Value handle = identifier.evaluate(scope, null);  
      String name = handle.getString();
      Signature signature = parameters.create(null);
      Function function = builder.create(signature, scope, name);
      
      functions.add(function);

      return null;
   }
   
}
