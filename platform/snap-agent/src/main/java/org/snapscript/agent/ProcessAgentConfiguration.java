package org.snapscript.agent;

public class ProcessAgentConfiguration {

   private String classPath;
   private String address;
   private int maxMemory;
   private int minMemory;
   
   public ProcessAgentConfiguration() {
      super();
   }

   public String getClassPath() {
      return classPath;
   }

   public void setClassPath(String classPath) {
      this.classPath = classPath;
   }
   
   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public int getMaxMemory() {
      return maxMemory;
   }

   public void setMaxMemory(int maxMemory) {
      this.maxMemory = maxMemory;
   }

   public int getMinMemory() {
      return minMemory;
   }

   public void setMinMemory(int minMemory) {
      this.minMemory = minMemory;
   }
 
}
