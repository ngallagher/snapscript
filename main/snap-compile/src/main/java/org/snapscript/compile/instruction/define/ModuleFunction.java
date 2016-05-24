package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.AnnotationList;
import org.snapscript.compile.instruction.Constraint;
import org.snapscript.compile.instruction.ConstraintExtractor;
import org.snapscript.compile.instruction.FunctionBuilder;
import org.snapscript.compile.instruction.ModifierList;
import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Function;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class ModuleFunction extends Statement {
   
   private final ConstraintExtractor constraint;
   private final AnnotationList annotations;
   private final ParameterList parameters;
   private final FunctionBuilder builder;
   private final NameExtractor extractor;
   private final ModifierList list;
   private final Statement body;
   
   public ModuleFunction(AnnotationList annotations, ModifierList list, Evaluation identifier, ParameterList parameters, Statement body){  
      this(annotations, list, identifier, parameters, null, body);
   }
   
   public ModuleFunction(AnnotationList annotations, ModifierList list, Evaluation identifier, ParameterList parameters, Constraint constraint, Statement body){  
      this.constraint = new ConstraintExtractor(constraint);
      this.extractor = new NameExtractor(identifier);
      this.builder = new FunctionBuilder(body);
      this.annotations = annotations;
      this.parameters = parameters;
      this.body = body;
      this.list = list;
   }  
   
   @Override
   public Result compile(Scope scope) throws Exception {
      Module module = scope.getModule();
      List<Function> functions = module.getFunctions();
      Signature signature = parameters.create(scope);
      String name = extractor.extract(scope);
      Type returns = constraint.extract(scope);
      Function function = builder.create(signature, returns, name);
      
      annotations.apply(scope, function);
      functions.add(function);
      body.compile(scope);
      
      return ResultType.getNormal(function);
   }
}