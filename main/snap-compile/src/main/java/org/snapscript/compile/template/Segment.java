package org.snapscript.compile.template;

import java.io.Writer;

import org.snapscript.core.Scope;

public interface Segment {
   void process(Scope scope, Writer writer) throws Exception; 
}
