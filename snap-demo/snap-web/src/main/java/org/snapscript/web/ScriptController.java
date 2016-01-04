package org.snapscript.web;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.web.resource.template.TemplateResult;

public class ScriptController {

   public TemplateResult index(Request request, Response response) throws Exception {
      return new TemplateResult("index.vm");
   }
}
