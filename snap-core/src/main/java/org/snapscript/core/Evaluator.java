package org.snapscript.core;

public interface Evaluator {
   <T> T evaluate(String source) throws Exception;
   <T> T evaluate(String source, Model model) throws Exception;
}
