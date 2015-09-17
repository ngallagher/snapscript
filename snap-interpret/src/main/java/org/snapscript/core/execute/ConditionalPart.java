package org.snapscript.core.execute;

public interface ConditionalPart{
   Evaluation getEvaluation();
   CombinationOperator getOperator();
}