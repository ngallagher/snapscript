package org.snapscript.engine.agent.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.snapscript.core.InstanceScope;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Value;

public class InstanceScopeNode implements ScopeNode {
   
   private final ScopeNodeBuilder builder;
   private final List<ScopeNode> nodes;
   private final Scope scope;
   private final String path;
   private final String name;
   
   public InstanceScopeNode(ScopeNodeBuilder builder, InstanceScope scope, String path, String name) {
      this.nodes = new ArrayList<ScopeNode>();
      this.builder = builder;
      this.scope = scope;
      this.name = name;
      this.path = path;
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
      if(nodes.isEmpty()) {
         State state = scope.getState();
         Set<String> names = state.getNames();
         
         if(!names.isEmpty()) {
            for(String name : names) {
               Value value = state.getValue(name);
               Object object = value.getValue();
               ScopeNode node = builder.createNode(path + "." + name, name, object);
               
               if(node != null) {
                  nodes.add(node);
               }
            }
         }
      }
      return nodes;
   }
}