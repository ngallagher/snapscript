package org.snapscript.web.resource.template;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

public interface TemplateController {
   TemplateResult handle(Request request, Response response) throws Exception;
}
