package org.snapscript.code.operation;

import org.snapscript.core.Evaluation;

public interface CalculationPart {
   Evaluation getEvaluation();
   NumericOperator getOperator();
}