package org.snapscript.engine.http.resource.template;

import java.io.Writer;

public interface Template {
   void render(TemplateFilter filter, Writer writer) throws Exception;
}
