package org.snapscript.engine.agent.debug;

import java.util.Collections;
import java.util.List;

public class ArrayScopeNode implements ScopeNode {
   
   private final ScopeNodeBuilder builder;
   private final Object object;
   private final String name;
   private final String path;
   private final int depth;
   
   public ArrayScopeNode(ScopeNodeBuilder builder, Object object, String path, String name, int depth) {
      this.builder = builder;
      this.object = object;
      this.depth = depth;
      this.path = path;
      this.name = name;
   }
   
   @Override
   public int getDepth() {
      return depth;
   }
   
   @Override
   public String getName() {
      return name;
   }
   
   @Override
   public String getPath() {
      return path;
   }

   @Override
   public List<ScopeNode> getNodes() {
      return Collections.emptyList();
   }
}