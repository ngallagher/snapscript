var statusProcesses = {};
var statusFocus = null;

function createStatus() {
   createRoute("STATUS", createStatusProcess, clearStatus); // status of processes
   createRoute("TERMINATE", terminateStatusProcess); // clear focus
   createRoute("EXIT", terminateStatusProcess);
}

function terminateStatusProcess(socket, type, text) {
   if(text != null) {
      statusProcesses[text] = null;
   }
   if(statusFocus == text) {
      suspendedThreads = {};
      currentFocusThread = null;
      terminateThreads();
      clearStatusFocus();
   }
   showStatus();
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
      $("#toolbarDebug").css('opacity', '1.0');
      $("#toolbarDebug").css('filter', 'alpha(opacity=100)'); // msie
      $("#process").html("<i>&nbsp;RUNNING: " + processResource + " ("+process+")</i>");
   }
   if(statusFocus != process) {
      clearProfiler(); // profiler does not apply
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
//   clearProfiler();
//   clearConsole();
   $("#toolbarDebug").css('opacity', '0.4');
   $("#toolbarDebug").css('filter', 'alpha(opacity=40)'); // msie
   $("#process").html("");
}

function clearStatus() {
   statusProcesses = {};
   statusFocus = null;
   w2ui['debug'].records = [];
   w2ui['debug'].refresh();
}

function showStatus() {
   var statusRecords = [];
   var statusIndex = 1;
   
   for (var statusProcess in statusProcesses) {
      if (statusProcesses.hasOwnProperty(statusProcess)) {
         var statusResource = statusProcesses[statusProcess];
         
         if(statusResource != null) {
            var resourcePathDetails = createResourcePath(statusResource);
            var displayName = "<div class='debugRecord'>"+statusProcess+"</div>";
   
            if(statusFocus == statusProcess) {
               displayName = "<div class='debugFocusRecord'>"+statusProcess+"</div>";
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
   w2ui['debug'].records = statusRecords;
   w2ui['debug'].refresh();
}

registerModule("status", "Status module: status.js", createStatus, [ "common", "socket", "tree" ]);