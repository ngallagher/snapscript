package org.snapscript.engine.agent.debug;

import java.util.List;

public interface ScopeNode {
   String getName();
   String getPath();
   List<ScopeNode> getNodes();
}
