package org.snapscript.core.execute;

public interface CalculationPart {
   Evaluation getEvaluation();
   NumericOperator getOperator();
}