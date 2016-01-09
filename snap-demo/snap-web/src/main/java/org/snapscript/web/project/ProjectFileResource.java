package org.snapscript.web.project;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.web.resource.ContentTypeResolver;
import org.snapscript.web.resource.Resource;

public class ProjectFileResource implements Resource {
   
   private final Map<String, Project> projects;
   private final ContentTypeResolver resolver;
   private final File workPath;
   
   public ProjectFileResource(ContentTypeResolver resolver, File workPath){
      this.projects = new ConcurrentHashMap<String, Project>();
      this.resolver = resolver;
      this.workPath = workPath;
   }

   @Override
   public void handle(Request request, Response response) throws Throwable {
      Path path = request.getPath(); // /project/<project-name>/<project-path>
      String projectPath = path.getPath(2); // /<project-name>/<project-path>
      String projectPrefix = path.getPath(1, 1); // /<project-name>
      String projectName = projectPrefix.substring(1); // <project-name>
      Project project = projects.get(projectName);
      
      if(project == null) {
         project = new Project(workPath, projectName);
         projects.put(projectName, project);
      }
      ProjectFileSystem fileSystem = project.getFileSystem();
      byte[] resource = fileSystem.readAsByteArray(projectPath);
      String type = resolver.resolveType(projectPath);
      OutputStream out = response.getOutputStream();
      
      response.setContentType(type);
      out.write(resource);
      out.close();
   }
}
