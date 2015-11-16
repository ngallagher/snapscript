package org.snapscript.compile.instruction;

public interface ConditionalPart{
   Evaluation getEvaluation();
   CombinationOperator getOperator();
}