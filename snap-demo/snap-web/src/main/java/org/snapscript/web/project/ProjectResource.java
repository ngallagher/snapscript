package org.snapscript.web.project;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.web.resource.Resource;
import org.snapscript.web.resource.template.TemplateEngine;
import org.snapscript.web.resource.template.TemplateModel;

public class ProjectResource implements Resource {
   
   private final TemplateEngine engine;
   private final String resource;
   
   public ProjectResource(TemplateEngine engine, String resource) {
      this.resource = resource;
      this.engine = engine;
   }
   
   public void handle(Request request, Response response) throws Exception {
      Map<String, Object> map = new HashMap<String, Object>();
      TemplateModel model = new TemplateModel(map);
      Path path = request.getPath(); // /project/<project-name>/<project-path>
      String projectPrefix = path.getPath(1, 2); // /<project-name>
      String projectName = projectPrefix.substring(1); // <project-name>
      map.put("project", projectName);
      String text = engine.renderTemplate(model, resource);
      PrintStream stream = response.getPrintStream();

      response.setContentType("text/html");
      stream.print(text);
      stream.close();
   }
}
