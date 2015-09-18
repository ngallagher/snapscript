package org.snapscript.interpret;

public interface ConditionalPart{
   Evaluation getEvaluation();
   CombinationOperator getOperator();
}