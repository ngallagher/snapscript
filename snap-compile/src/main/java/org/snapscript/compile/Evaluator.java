package org.snapscript.compile;

public interface Evaluator {
   <T> T evaluate(String source) throws Exception;
}
