
function startTelemetry() {
   createRoute("PROFILE", updateTelemetry)
}

function updateTelemetry(socket, type, text) {
   var profileResult = JSON.parse(text);
   var profileRecords = profileResult.results;
   var editorData = loadEditor();
   var telemetryRecords = [];
   var telemetryIndex = 1;
   
   for(var i = 0; i < profileRecords.length; i++) {
      var profileRecord = profileRecords[i];
      
      telemetryRecords.push({
         recid: telemetryIndex++,
         resource: editorData.resource.projectPath,
         duration: profileRecord.time,
         line: profileRecord.line,
         count: profileRecord.count,
         script: editorData.resource.resourcePath
      });
   }
   //console.log(text);
   w2ui['telemetry'].records = telemetryRecords;
   w2ui['telemetry'].refresh();
}

registerModule("telemetry", "Telemetry module: telemetry.js", startTelemetry, [ "common", "socket" ]);