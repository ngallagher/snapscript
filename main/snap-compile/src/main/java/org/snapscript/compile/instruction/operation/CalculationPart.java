package org.snapscript.compile.instruction.operation;

import org.snapscript.core.Evaluation;

public interface CalculationPart {
   Evaluation getEvaluation();
   NumericOperator getOperator();
}