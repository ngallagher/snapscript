package org.snapscript.engine;

import java.util.regex.Pattern;

public enum ProcessEngineArgument {
   AGENT_PORT("agent-port", "0", "Port for agent connections", "\\d+"),
   AGENT_POOL("agent-pool", "4", "Number of agents in pool", "\\d+"),
   PORT("port", "0", "Port for HTTP connections", "\\d+"),
   MODE("mode", "develop", "Mode to start on", "(develop|debug|run)"),
   DIRECTORY("directory", "work", "Directory used for sources", ".*"),
   SCRIPT("script", null, "Script to launch", ".*.snap");
   
   public final String description;
   public final Pattern pattern;
   public final String command;
   public final String value;
   
   private ProcessEngineArgument(String command, String value, String description, String pattern) {
      this.pattern = Pattern.compile(pattern);
      this.description = description;
      this.command = command;
      this.value = value;
   }
   
   public String getValue() {
      return System.getProperty(command);
   }
   
   public static Pattern getPattern(String command) {
      ProcessEngineArgument[] arguments = ProcessEngineArgument.values();
      
      for(ProcessEngineArgument argument : arguments) {
         String name = argument.command;
         
         if(name.equals(command)) {
            return argument.pattern;
         }
      }
      return null;
   }
}
