package org.snapscript.engine.http.project;

import java.io.File;
import java.io.PrintStream;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.engine.http.resource.Resource;

public class ProjectTreeResource implements Resource {
   
   private final ProjectBuilder builder;
   
   public ProjectTreeResource(ProjectBuilder builder) {
      this.builder = builder;
   }

   @Override
   public void handle(Request request, Response response) throws Throwable {
      String name = request.getParameter("id");
      String expand = request.getParameter("expand");
      String folders = request.getParameter("folders");
      Path path = request.getPath(); // /tree/<project-name>
      Project project = builder.createProject(path);
      String projectName = project.getProjectName();
      File projectPath = project.getProjectPath();
      StringBuilder builder = new StringBuilder();
      builder.append("<div id=\""+name+"\">\n");
      builder.append("<ul id=\"treeData\" style=\"display: none;\">\n");
      DirectoryTree tree = new DirectoryTree(
               projectPath, 
               projectName, 
               expand,
               folders);
      tree.buildTree(builder);
      builder.append("</ul>\n");
      builder.append("</div>\n");
      String result = builder.toString();
      System.err.println(result);
      PrintStream out = response.getPrintStream();
      response.setContentType("text/html");
      out.println(result);
      out.close();
   }
   
   public static final class DirectoryTree {
      
      private final File root;
      private final String project;
      private final String expand;
      private final String folders;
      
      public DirectoryTree(File root, String project) {
         this(root, project, null);
      }
      
      public DirectoryTree(File root, String project, String expand) {
         this(root, project, expand, null);
      }
      
      public DirectoryTree(File root, String project, String expand, String folders) {
         this.root = root;
         this.project = project;
         this.expand = expand;
         this.folders = folders;
      }
      
      public void buildTree(StringBuilder builder) throws Exception {
         String expandPath = null;
         boolean foldersOnly = false;
         
         if(folders != null) {
            foldersOnly = Boolean.parseBoolean(folders);
         }
         if(expand != null) {
            expandPath = expand;

            if(expandPath.startsWith("/")) {
               expandPath = expandPath.substring(1); 
            } 
            if(expandPath.endsWith("/")) {
               int length = expandPath.length();
               expandPath = expand.substring(0, length - 1);
            }
            expandPath = "/resource/" + project + "/" + expandPath;
         }
         if(expandPath != null) {
            buildTree(builder, root, expandPath, "/resource/" + project, "  ", "id", 1, foldersOnly, true);
         } else {
            buildTree(builder, root, expandPath, "/resource/" + project, "  ", "id", 1, foldersOnly, false);
         }
      }
   
      private void buildTree(StringBuilder builder, File currentFile, String expandPath, String currentPath, String pathIndent, String idPrefix, int id, boolean foldersOnly, boolean openPath) throws Exception {
         String name = currentFile.getName();
         if(currentFile.isDirectory()) {
            builder.append(pathIndent);
            builder.append("<li id=\"");
            builder.append(idPrefix);
            builder.append(id);
            builder.append("\" title=\"");
            builder.append(currentPath);
            
            if(openPath) {
               builder.append("\" data-icon=\"/img/toolbar/fldr_obj.gif\" class=\"expanded folder\">");
            } else {
               builder.append("\" data-icon=\"/img/toolbar/fldr_obj.gif\" class=\"folder\">");
            }
            builder.append(name);
            builder.append("\n");
            
            File[] list = currentFile.listFiles();
            if(list != null && list.length > 0) {
               idPrefix = idPrefix + id + ".";
               builder.append(pathIndent);
               builder.append("<ul>\n");
               for(int i = 0; i < list.length; i++) {
                  File entry = list[i];
                  String title = entry.getName();
                  String nextPath = currentPath + "/" + title;
                  
                  if(expandPath == null || !expandPath.startsWith(nextPath)) {
                     buildTree(builder, entry, expandPath, nextPath, pathIndent + "  ", idPrefix, i + 1, foldersOnly, false);
                  } else {
                     buildTree(builder, entry, expandPath, nextPath, pathIndent + "  ", idPrefix, i + 1, foldersOnly, true);
                  }
               }
               builder.append(pathIndent);
               builder.append("</ul>\n");
            }
         } else {
            if(!foldersOnly) {
               String icon = "data-icon=\"/img/toolbar/cu_obj.gif\"";
               
               if(name.endsWith(".gif")) {
                  icon = "data-icon=\"/img/toolbar/image_obj.gif\"";
               } else if(name.endsWith(".png")) {
                  icon = "data-icon=\"/img/toolbar/image_obj.gif\"";
               } else if(name.endsWith(".jpg")) {
                  icon = "data-icon=\"/img/toolbar/image_obj.gif\"";    
               } else if(name.endsWith(".jar")) {
                  icon = "data-icon=\"/img/toolbar/jar_src_obj.gif\"";                    
               }else if(!name.endsWith(".snap")){
                  icon = "data-icon=\"/img/toolbar/file_obj.gif\"";
               }
               builder.append(pathIndent);
               builder.append("<li ");
               builder.append(icon);
               builder.append(" id=\"");
               builder.append(idPrefix);
               builder.append(id);
               builder.append("\" title=\"");
               builder.append(currentPath);
               builder.append("\">");
               builder.append(name);
               builder.append("\n");
            }
         }
      }
   }

}
