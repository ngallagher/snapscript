package org.snapscript.android.agent;

import java.net.URI;

import org.snapscript.agent.ProcessAgent;

import android.app.Activity;
import android.os.Bundle;

public class ScriptAgent extends Activity {

   public static final String REMOTE_HOST = "192.168.1.66";
   public static final int HTTP_PORT = 4457;
   public static final int EVENT_PORT = 4456;
   
   @Override
   public void onCreate(Bundle bundle) {
      super.onCreate(bundle);
      try {
         ProcessAgent agent = new ProcessAgent(
               URI.create("http://"+REMOTE_HOST+":"+HTTP_PORT+"/resource"), 
               "android-" + System.currentTimeMillis(), 
               EVENT_PORT);
         agent.run();
      } catch (Exception e) {
         e.printStackTrace();
      }

   }
}
