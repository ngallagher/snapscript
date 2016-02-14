package org.snapscript.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProcessCommandLine {

   private static enum CommandArgument {
      CLIENT_PORT("client-port", "4457", "Port for HTTP connections"),
      AGENT_PORT("agent-port", "4456", "Port for agent connections"),
      AGENT_POOL("agent-pool", "4", "Number of agents in pool"),
      SUSPEND("suspend", "false", "Suspend until HTTP connection established"),
      DIRECTORY("directory", "work", "Directory used for sources"),
      SCRIPT("script", null, "Script to execute");
      
      private final String description;
      private final String command;
      private final String value;
      
      private CommandArgument(String command, String value, String description) {
         this.description = description;
         this.command = command;
         this.value = value;
      }
   }
   
   public static void main(String[] list) throws Exception {
      Map<String, String> commands = new HashMap<String, String>();
      CommandArgument[] arguments = CommandArgument.values();
      
      for(CommandArgument argument : arguments) {
         String name = argument.command;
         String value = argument.value;
         
         if(value != null) {
            commands.put(name, value);
            System.setProperty(name, value);
         }
      }
      for(String argument : list) {
         if(argument.startsWith("--")) {
            argument = argument.substring(2);
         }
         String[] pair = argument.split("=");
         String name = pair[0].trim();
         String value = pair[1].trim();
         
         if(value.startsWith("\"") && value.endsWith("\"")) {
            int length = value.length();
            value = value.substring(1, length - 1);
         }
         commands.put(name, value);
         System.setProperty(name, value);
      }
      Set<String> names = commands.keySet();
      
      for(String name : names) {
         String value= commands.get(name);
         System.out.println("--" + name + "=" + value);
      }
      ProcessEngineContext service = new ProcessEngineContext("/etc/spring.xml");
      service.start();
   }
}
