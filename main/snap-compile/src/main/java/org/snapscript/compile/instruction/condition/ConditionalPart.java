package org.snapscript.compile.instruction.condition;

import org.snapscript.core.Evaluation;

public interface ConditionalPart{
   Evaluation getEvaluation();
   CombinationOperator getOperator();
}