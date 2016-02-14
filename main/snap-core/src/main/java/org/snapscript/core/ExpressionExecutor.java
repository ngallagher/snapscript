package org.snapscript.core;

public interface ExpressionExecutor {
   <T> T execute(Scope scope, String module, String source) throws Exception;
}
