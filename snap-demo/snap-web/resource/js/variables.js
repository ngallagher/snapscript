
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