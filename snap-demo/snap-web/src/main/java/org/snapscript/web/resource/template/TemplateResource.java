package org.snapscript.web.resource.template;

import java.io.PrintStream;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.web.resource.Resource;

public class TemplateResource implements Resource {
   
   private final TemplateController controller;
   private final TemplateEngine engine;
   
   public TemplateResource(TemplateEngine engine, TemplateController controller) {
      this.controller = controller;
      this.engine = engine;
   }

   @Override
   public void handle(Request request, Response response) throws Throwable {
      TemplateResult result = controller.handle(request, response);
      TemplateModel model = result.getModel();
      String template = result.getTemplate();
      PrintStream stream = response.getPrintStream();
      String text = engine.renderTemplate(model, template);
      response.setContentType("text/html");
      stream.print(text);
      stream.close();
   }

}
