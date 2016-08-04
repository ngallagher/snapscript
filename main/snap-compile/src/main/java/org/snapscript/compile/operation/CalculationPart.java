package org.snapscript.compile.operation;

import org.snapscript.core.Evaluation;

public interface CalculationPart {
   Evaluation getEvaluation();
   NumericOperator getOperator();
}