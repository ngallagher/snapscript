package org.snapscript.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessEngineLauncher {
   
   public static void main(String[] list) throws Exception {
      Map<String, String> commands = new HashMap<String, String>();
      ProcessEngineArgument[] arguments = ProcessEngineArgument.values();
      
      for(ProcessEngineArgument argument : arguments) {
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
         if(value != null) {
            Pattern pattern = ProcessEngineArgument.getPattern(name);
            
            if(pattern != null) {
               Matcher matcher = pattern.matcher(value);
               
               if(!matcher.matches()) {
                  System.out.println("--"+name+"="+value+ " does not match pattern "+pattern);
               }
               commands.put(name, value);
               System.setProperty(name, value);
            }
         }
      }
      Set<String> names = commands.keySet();
      
      for(String name : names) {
         String value= commands.get(name);
         System.out.println("--" + name + "=" + value);
      }
      ProcessEngineContext service = new ProcessEngineContext("/context/application.xml");
      service.start();
   }
}
