var statusProcesses = {};
var statusFocus = null;

function createStatus() {
   createRoute("STATUS", createStatusProcess, clearStatus); // status of processes
   createRoute("TERMINATE", terminateStatusProcess); // clear focus
   createRoute("EXIT", terminateStatusProcess);
}

function terminateStatusProcess(socket, type, text) {
   if(statusFocus == text) {
      suspendedThreads = {};
      currentFocusThread = null;
      terminateThreads();
      clearStatusFocus();
   }
   statusProcesses[text] = null;
}

function createStatusProcess(socket, type, text) { // process is running
   var message = JSON.parse(text);
   var process = message.process;
   var processResource = message.resource;
   var processFocus = "" + message.focus;
   
   statusProcesses[process] = processResource;
   
   if(processFocus == "true") {
      updateStatusFocus(process);
   } else {
      if(statusFocus == process) {
         clearStatusFocus(); // we are focus = false
      }
   }
   showStatus();
}

function currentStatusFocus() {
   return statusFocus;
}

function updateStatusFocus(process) {
   var processResource = statusProcesses[process];
   
   if(processResource != null){
      $("#process").html("<i>&nbsp;RUNNING: " + processResource + " ("+process+")</i>");
   }
   if(statusFocus != process) {
      clearTelemetry(); // telemetry does not apply
      clearThreads(); // race condition here
      clearVariables();
   }
   updateConsoleFocus(process); // clear console if needed
   statusFocus = process;
}

function clearStatusFocus(){ // clear up stuff
   statusFocus = null;
   clearThreads(); // race condition here
   clearVariables();
//   clearTelemetry();
//   clearConsole();
   $("#process").html("");
}

function clearStatus() {
   statusProcesses = {};
   statusFocus = null;
   w2ui['status'].records = [];
   w2ui['status'].refresh();
}

function showStatus() {
   var statusRecords = [];
   var statusIndex = 1;
   
   for (var statusProcess in statusProcesses) {
      if (statusProcesses.hasOwnProperty(statusProcess)) {
         var statusResource = statusProcesses[statusProcess];
         
         if(statusResource != null) {
            var resourcePathDetails = createResourcePath(statusResource);
            var displayName = "<div class='statusRecord'>"+statusProcess+"</div>";
   
            if(statusFocus == statusProcess) {
               displayName = "<div class='statusFocusRecord'>"+statusProcess+"</div>";
            }
            statusRecords.push({
               recid: statusIndex++,
               name: displayName,
               process: statusProcess,
               resource: statusResource,
               focus: statusFocus == statusProcess,
               script: resourcePathDetails.resourcePath
            });
         }
      }
   }
   w2ui['status'].records = statusRecords;
   w2ui['status'].refresh();
}

registerModule("status", "Status module: status.js", createStatus, [ "common", "socket", "tree" ]);