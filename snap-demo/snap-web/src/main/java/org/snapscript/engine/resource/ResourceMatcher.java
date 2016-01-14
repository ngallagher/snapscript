package org.snapscript.engine.resource;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

public interface ResourceMatcher {
   Resource match(Request request, Response response) throws Exception;
}