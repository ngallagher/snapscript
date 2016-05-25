package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.AnnotationList;
import org.snapscript.compile.instruction.Constraint;
import org.snapscript.compile.instruction.ConstraintExtractor;
import org.snapscript.compile.instruction.ModifierChecker;
import org.snapscript.compile.instruction.ModifierList;
import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class MemberFunctionAssembler {
   
   private final ConstraintExtractor constraint;
   private final ParameterList parameters;
   private final ModifierChecker checker;
   private final NameExtractor extractor;
   private final ModifierList list;
   private final Statement body;
   
   public MemberFunctionAssembler(ModifierList list, Evaluation identifier, ParameterList parameters, Constraint constraint, Statement body){ 
      this.constraint = new ConstraintExtractor(constraint);
      this.extractor = new NameExtractor(identifier);
      this.checker = new ModifierChecker(list);
      this.parameters = parameters;
      this.list = list;
      this.body = body;
   } 

   public MemberFunctionBuilder assemble(Scope scope, Type type, int mask) throws Exception {
      String name = extractor.extract(scope);
      Signature signature = parameters.create(scope);
      Type returns = constraint.extract(scope);
      int modifiers = mask | list.getModifiers();
      
      if(checker.isStatic()) {
         return new StaticFunctionBuilder(signature, body, returns, name, modifiers);
      }
      return new InstanceFunctionBuilder(signature, body, returns, name, modifiers);
      
   }
}
