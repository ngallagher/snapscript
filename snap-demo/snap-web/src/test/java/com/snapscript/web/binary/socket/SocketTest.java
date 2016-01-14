package com.snapscript.web.binary.socket;

import java.io.PrintStream;

import junit.framework.TestCase;

import org.snapscript.engine.agent.ProcessAgentStream;
import org.snapscript.engine.event.ExitEvent;
import org.snapscript.engine.event.ProcessEventAdapter;
import org.snapscript.engine.event.ProcessEventChannel;
import org.snapscript.engine.event.ProcessEventType;
import org.snapscript.engine.event.RegisterEvent;
import org.snapscript.engine.event.WriteErrorEvent;
import org.snapscript.engine.event.socket.SocketEventClient;
import org.snapscript.engine.event.socket.SocketEventServer;

public class SocketTest extends TestCase {

   private class DemoListener extends ProcessEventAdapter {
      
      private final String name;
      
      public DemoListener(String name) {
         this.name = name;
      }
      
      @Override
      public void onWriteError(ProcessEventChannel channel, WriteErrorEvent event) throws Exception {
         try {               
            byte[] array = event.getData();
            int length = event.getLength();
            int offset = event.getOffset();
            String text = new String(array, offset, length, "UTF-8");
            System.err.println("TEXT: ["+text+"]");
         } catch(Exception e) {
            e.printStackTrace();
         }
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
      ProcessAgentStream stream = new ProcessAgentStream(ProcessEventType.WRITE_ERROR, channel, "XXX");
      PrintStream printer = new PrintStream(stream, true, "UTF-8");
      printer.println("line-1");
      printer.println("line-2");
      Thread.sleep(100000);
   }
}
