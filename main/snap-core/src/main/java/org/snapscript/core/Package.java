package org.snapscript.core;

public interface Package {
   Statement compile(Scope scope) throws Exception;
}
