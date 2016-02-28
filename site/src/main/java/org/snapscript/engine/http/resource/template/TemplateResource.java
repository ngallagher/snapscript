package org.snapscript.engine.http.resource.template;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.engine.http.resource.Resource;
import org.snapscript.engine.http.resource.template.TemplateEngine;
import org.snapscript.engine.http.resource.template.TemplateModel;

public class TemplateResource implements Resource {
   
   private final TemplateEngine engine;
   
   public TemplateResource(TemplateEngine engine) {
      this.engine = engine;
   }
   
   public void handle(Request request, Response response) throws Exception {
      Map<String, Object> map = new HashMap<String, Object>();
      TemplateModel model = new TemplateModel(map);
      Path path = request.getPath(); 
      String normal = path.getPath();
      PrintStream stream = response.getPrintStream();
      String text = engine.renderTemplate(model, normal);

      response.setContentType("text/html");
      stream.print(text);
      stream.close();
   }
}
