package org.snapscript.engine.http.project;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.engine.http.resource.Resource;
import org.snapscript.engine.http.resource.template.TemplateEngine;
import org.snapscript.engine.http.resource.template.TemplateModel;

public class ProjectOpenDialog implements Resource {
   
   private final TemplateEngine engine;
   private final String resource;
   private final File root;
   
   public ProjectOpenDialog(TemplateEngine engine, String resource, File root) {
      this.resource = resource;
      this.engine = engine;
      this.root = root;
   }
   
   public void handle(Request request, Response response) throws Exception {
      Map<String, Object> map = new HashMap<String, Object>();
      TemplateModel model = new TemplateModel(map);
      String name = root.getName();
      map.put("root", name);
      String text = engine.renderTemplate(model, resource);
      PrintStream stream = response.getPrintStream();

      response.setContentType("text/html");
      stream.print(text);
      stream.close();
   }
}
