
function showVariables() {
   var threadVariables = focusedThreadVariables();
   var variableRecords = [];
   var variableIndex = 1;
   
   for (var variableName in threadVariables) {
      if (threadVariables.hasOwnProperty(variableName)) {
         variableRecords.push({
            recid: variableIndex++,
            name: variableName,
            value: threadVariables[variableName]
         });
      }
   }
   w2ui['variables'].records = variableRecords;
   w2ui['variables'].refresh();
}