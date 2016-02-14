package org.snapscript.compile;

import org.snapscript.core.Model;

public interface Executable {   
   void execute() throws Exception;
   void execute(Model model) throws Exception;
}
