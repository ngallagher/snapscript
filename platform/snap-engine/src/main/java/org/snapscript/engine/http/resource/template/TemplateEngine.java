package org.snapscript.engine.http.resource.template;

public interface TemplateEngine {
   String renderTemplate(TemplateModel model, String template) throws Exception;
   boolean validTemplate(String template) throws Exception;
}
