package org.snapscript.develop;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface Configuration {
   boolean isValidate();
   Map<String, String> getVariables();
   List<File> getDependencies();
   List<String> getArguments();
}
