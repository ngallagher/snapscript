package org.snapscript.engine.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.SocketProcessor;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description="Web server")
public class WebServer {

   private final SocketProcessor server;
   private final SocketAddress address;
   private final Connection connection;

   public WebServer(Container container, int port) throws IOException {
      this.server = new ContainerSocketProcessor(container, 2);
      this.connection = new SocketConnection(server);
      this.address = new InetSocketAddress(port);
   }

   @ManagedOperation(description="Start the server")
   public void start() throws IOException {
	   try {
		   connection.connect(address);
	   } catch (IOException ex) {
		   System.err.println("Failed to connect to: " + address);
		   throw ex;
	   }
   }

   @ManagedOperation(description="Stop the server")
   public void stop() throws IOException {
      connection.close();
   }
}
