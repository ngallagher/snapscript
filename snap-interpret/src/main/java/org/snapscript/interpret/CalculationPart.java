package org.snapscript.interpret;

public interface CalculationPart {
   Evaluation getEvaluation();
   NumericOperator getOperator();
}