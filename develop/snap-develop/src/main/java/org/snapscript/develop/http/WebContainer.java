package org.snapscript.develop.http;

import static org.simpleframework.http.Protocol.DATE;
import static org.simpleframework.http.Protocol.SERVER;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebContainer implements Container {

   private static final Logger LOG = LoggerFactory.getLogger(WebContainer.class);

   private final Container container;
   private final String name;

   public WebContainer(Container container, String name) {
      this.container = container;
      this.name = name;
   }

   @Override
   public void handle(Request req, Response resp) {
      long time = System.currentTimeMillis();

      try {
         resp.setDate(DATE, time);
         resp.setValue(SERVER, name);
         container.handle(req, resp);
      } catch (Throwable cause) {
         LOG.info("Internal server error", cause);

         try {
            resp.close();
         } catch (Exception ignore) {
            LOG.info("Could not close response", ignore);
         }
      }
   }

}
