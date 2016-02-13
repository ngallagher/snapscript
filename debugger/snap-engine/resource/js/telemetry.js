
function startTelemetry() {
   createRoute("PROFILE", updateTelemetry)
}

function updateTelemetry(socket, type, text) {
   var profileResult = JSON.parse(text);
   var profileRecords = profileResult.results;
   var telemetryRecords = [];
   var telemetryWidths = [];
   var telemetryIndex = 1;
   var totalTime = 0;
  
   for(var i = 0; i < profileRecords.length; i++) {
      totalTime += profileRecords[i].time;
   }
   for(var i = 0; i < profileRecords.length; i++) {
      var recordTime = profileRecords[i].time;
      
      if(recordTime > 0) {
         var percentageTime = (recordTime/totalTime)*100;
         var percentage = parseInt(percentageTime);
         
         telemetryWidths[i] = percentage;
      }
   }
   for(var i = 0; i < profileRecords.length; i++) {
      var profileRecord = profileRecords[i];
      var resourcePath = createResourcePath(profileRecord.resource);
      var displayName = "<div class='telemetryRecord'>"+resourcePath.projectPath+"</div>";
      var percentageBar = "<div style='padding: 2px;'><div style='height: 10px; background: #C61414; width: "+telemetryWidths[i]+"%;'></div></div>";
      
      telemetryRecords.push({
         recid: telemetryIndex++,
         resource: displayName,
         percentage: percentageBar,
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