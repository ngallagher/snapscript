package org.snapscript.engine.resource;

import static org.simpleframework.http.Protocol.CONTENT_TYPE;
import static org.simpleframework.http.Status.OK;

import java.io.File;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;

public class FileSystemResource implements Resource {

   private final ContentTypeResolver typeResolver;
   private final FileResolver fileResolver;
   private final Status status;

   public FileSystemResource(FileResolver fileResolver, ContentTypeResolver typeResolver) {
      this(fileResolver, typeResolver, OK);
   }

   public FileSystemResource(FileResolver fileResolver, ContentTypeResolver typeResolver, Status status) {
      this.fileResolver = fileResolver;
      this.typeResolver = typeResolver;
      this.status = status;
   }

   @Override
   public void handle(Request request, Response response) throws Exception {
      Path path = request.getPath();
      String target = path.getPath();
      File file = fileResolver.resolveFile(target);
      String type = typeResolver.resolveType(file);
      FileChannel channel = fileResolver.resolveChannel(target);
      WritableByteChannel output = response.getByteChannel();
      long length = channel.size();

      response.setCode(status.code);
      response.setDescription(status.description);
      response.setValue(CONTENT_TYPE, type);
      response.setContentLength(length);
      channel.transferTo(0, length, output);
      channel.close();
      output.close();
   }
}
