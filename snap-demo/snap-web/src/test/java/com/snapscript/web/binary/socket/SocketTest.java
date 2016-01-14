package com.snapscript.web.binary.socket;

import junit.framework.TestCase;

import org.snapscript.web.binary.event.ProcessEventAdapter;
import org.snapscript.web.binary.event.ProcessEventChannel;
import org.snapscript.web.binary.event.ExitEvent;
import org.snapscript.web.binary.event.RegisterEvent;
import org.snapscript.web.binary.socket.SocketEventClient;
import org.snapscript.web.binary.socket.SocketEventServer;

public class SocketTest extends TestCase {

   private class DemoListener extends ProcessEventAdapter {
      
      private final String name;
      
      public DemoListener(String name) {
         this.name = name;
      }
      
      @Override
      public void onRegister(ProcessEventChannel channel, RegisterEvent event) throws Exception  {
         System.err.println("REGISTER: process=" + event.getProcess() + " name="+name);
         channel.send(new ExitEvent("exit["+name+"]="+event.getProcess()));
      }
      
      @Override
      public void onExit(ProcessEventChannel channel, ExitEvent event) throws Exception {
         System.err.println("EXIT: process=" + event.getProcess() + " name="+name);
      }
   }
   
   public void testSocket() throws Exception {
      SocketEventServer server = new SocketEventServer(new DemoListener("server-listener"), 3344);
      SocketEventClient client = new SocketEventClient(new DemoListener("client-listener"));
      
      server.start();
      ProcessEventChannel channel = client.connect(3344);
      
      for(int i = 0; i < 100; i++) {
         channel.send(new RegisterEvent("blah-" + i, System.getProperty("os.name")));
      }
      Thread.sleep(100000);
   }
}
