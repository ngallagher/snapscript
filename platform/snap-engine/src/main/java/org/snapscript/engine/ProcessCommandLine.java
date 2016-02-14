package org.snapscript.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessCommandLine {

   private static enum CommandArgument {
      CLIENT_PORT("client-port", "4457", "Port for HTTP connections", "\\d+"),
      AGENT_PORT("agent-port", "4456", "Port for agent connections", "\\d+"),
      AGENT_POOL("agent-pool", "4", "Number of agents in pool", "\\d+"),
      SUSPEND("suspend", "false", "Suspend until HTTP connection established", "(true|false)"),
      DIRECTORY("directory", "work", "Directory used for sources", ".*"),
      SCRIPT("script", null, "Script to execute", ".*.snap"),
      MODE("mode", "develop", "Mode to start with", "(develop|debug)");
      
      private final String description;
      private final Pattern pattern;
      private final String command;
      private final String value;
      
      private CommandArgument(String command, String value, String description, String pattern) {
         this.pattern = Pattern.compile(pattern);
         this.description = description;
         this.command = command;
         this.value = value;
      }
      
      public String getValue() {
         return System.getProperty(command);
      }
      
      public static Pattern getPattern(String command) {
         CommandArgument[] arguments = CommandArgument.values();
         
         for(CommandArgument argument : arguments) {
            String name = argument.command;
            
            if(name.equals(command)) {
               return argument.pattern;
            }
         }
         return null;
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
         Pattern pattern = CommandArgument.getPattern(name);
         Matcher matcher = pattern.matcher(value);
         
         if(!matcher.matches()) {
            System.out.println("--"+name+"="+value+ " does not match pattern "+pattern);
         }
         commands.put(name, value);
         System.setProperty(name, value);
      }
      Set<String> names = commands.keySet();
      
      for(String name : names) {
         String value= commands.get(name);
         System.out.println("--" + name + "=" + value);
      }
      String mode = CommandArgument.MODE.getValue();
      
      if(mode == null) {
         throw new IllegalArgumentException("Mode not configured");
      }
      ProcessEngineContext service = new ProcessEngineContext("/mode/" + mode + ".xml");
      service.start();
   }
}
