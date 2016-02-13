package org.snapscript.compile.instruction.condition;

import org.snapscript.compile.instruction.Evaluation;

public interface ConditionalPart{
   Evaluation getEvaluation();
   CombinationOperator getOperator();
}