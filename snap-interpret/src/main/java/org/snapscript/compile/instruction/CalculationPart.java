package org.snapscript.compile.instruction;

public interface CalculationPart {
   Evaluation getEvaluation();
   NumericOperator getOperator();
}