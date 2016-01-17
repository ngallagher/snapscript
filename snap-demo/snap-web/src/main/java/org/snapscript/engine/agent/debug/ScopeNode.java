package org.snapscript.engine.agent.debug;

import java.util.List;

public interface ScopeNode {
   String NAME_KEY = "name";
   String TYPE_KEY = "type";
   String VALUE_KEY = "value";
   String EXPANDABLE_KEY = "expandable";
   String DEPTH_KEY = "depth";
   int getDepth();
   String getName();
   String getPath();
   List<ScopeNode> getNodes();
}
