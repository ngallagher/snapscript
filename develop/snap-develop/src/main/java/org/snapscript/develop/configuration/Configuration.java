package org.snapscript.develop.configuration;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface Configuration {
   Map<String, String> getVariables();
   List<File> getDependencies();
   List<String> getArguments();
}
