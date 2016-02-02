
function startTelemetry() {
   createRoute("PROFILE", updateTelemetry)
}

function updateTelemetry(socket, type, text) {
   var profileResult = JSON.parse(text);
   var profileRecords = profileResult.results;
   var telemetryRecords = [];
   var telemetryIndex = 1;
   
   for(var i = 0; i < profileRecords.length; i++) {
      var profileRecord = profileRecords[i];
      var resourcePath = createResourcePath(profileRecord.resource);
      var displayName = "<div class='telemetryRecord'>"+resourcePath.projectPath+"</div>";
      
      telemetryRecords.push({
         recid: telemetryIndex++,
         resource: displayName,
         duration: profileRecord.time,
         line: profileRecord.line,
         count: profileRecord.count,
         script: resourcePath.resourcePath
      });
   }
   //console.log(text);
   w2ui['telemetry'].records = telemetryRecords;
   w2ui['telemetry'].refresh();
}

function clearTelemetry() {
   w2ui['telemetry'].records = [];
   w2ui['telemetry'].refresh();
}

registerModule("telemetry", "Telemetry module: telemetry.js", startTelemetry, [ "common", "socket" ]);