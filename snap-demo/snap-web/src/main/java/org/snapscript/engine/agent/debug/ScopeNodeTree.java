package org.snapscript.engine.agent.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Value;

public class ScopeNodeTree implements ScopeNode {
   
   private final ScopeNodeBuilder builder;
   private final List<ScopeNode> nodes;
   private final Scope scope;
   
   public ScopeNodeTree(Map<String, String> variables, Scope scope) {
      this.builder = new ScopeNodeBuilder(variables);
      this.nodes = new ArrayList<ScopeNode>();
      this.scope = scope;
   }
   
   @Override
   public String getName() {
      return "";
   }
   
   @Override
   public String getPath() {
      return "";
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
               ScopeNode node = builder.createNode(name, name, object);
               
               if(node != null) {
                  nodes.add(node);
               }
            }
         }
      }
      return nodes;
   }
}