var threadVariables = {};

function toggleExpandVariable(name) {
   var threadScope = focusedThread();
   var expandPath = name + ".*"; // this ensures they sort in sequence with '.' notation, e.g blah.foo.*
   
   if(threadScope != null) {
      var variables = threadVariables[threadScope.thread];
      if(variables == null) {
         variables = [];
         threadVariables[threadScope.thread] = variables;
      }
      var contains = false;
      
      for(var i = 0; i< variables.length; i++) {
         var value = variables[i];
         if(variables == expandPath) {
            contains = true;
            variables.splice(i, 1); // remove variable
            break;
         }
      }
      if(!contains) {
         variables.push(expandPath); // add variable
      }
      browseScriptVariables(variables);
   }
}

function showVariables() {
   var threadVariables = focusedThreadVariables();
   var sortedNames = [];
   var variableRecords = [];
   var variableIndex = 1;
   
   for (var variableName in threadVariables) {
      if (threadVariables.hasOwnProperty(variableName)) {
         sortedNames.push(variableName);
      }
   }
   sortedNames.sort();
   
   for(var i = 0; i < sortedNames.length; i++) {
      var variableName = sortedNames[i];
      
      variableRecords.push({
         recid: variableIndex++,
         name: variableName,
         value: threadVariables[variableName]
      });
   }
   w2ui['variables'].records = variableRecords;
   w2ui['variables'].refresh();
}