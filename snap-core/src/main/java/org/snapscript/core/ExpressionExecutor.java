package org.snapscript.core;

public interface ExpressionExecutor {
   <T> T execute(Scope scope, String source) throws Exception;
}
