package org.snapscript.compile.instruction.operation;

import org.snapscript.compile.instruction.Evaluation;

public interface CalculationPart {
   Evaluation getEvaluation();
   NumericOperator getOperator();
}