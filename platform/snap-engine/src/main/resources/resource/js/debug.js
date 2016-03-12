var statusProcesses = {};
var statusFocus = null;

function createStatus() {
   createRoute("STATUS", createStatusProcess, clearStatus); // status of processes
   createRoute("TERMINATE", terminateStatusProcess); // clear focus
   createRoute("EXIT", terminateStatusProcess);
   setInterval(refreshStatusProcesses, 1000); // refresh the status systems every 1 second
}

function refreshStatusProcesses() {
   var timeMillis = currentTime();
   var activeProcesses = {};
   var expiryCount = 0;
   
   for (var statusProcess in statusProcesses) {
      if (statusProcesses.hasOwnProperty(statusProcess)) {
         var statusProcessInfo = statusProcesses[statusProcess];
         
         if(statusProcessInfo != null) {
            if(statusProcessInfo.time + 10000 > timeMillis) {
               activeProcesses[statusProcess] = statusProcessInfo;
            } else {
               expiryCount++;
            }
         }
      }
   }
   statusProcesses = activeProcesses; // reset refreshed statuses
   
   if(expiryCount > 0) {
      showStatus(); // something expired!
   }
}

function findStatusWaitingProcessSystem() {
   for (var statusProcess in statusProcesses) {
      if (statusProcesses.hasOwnProperty(statusProcess)) {
         var statusProcessInfo = statusProcesses[statusProcess];
         
         if(statusProcessInfo != null) {
            if(!statusProcessInfo.running) {
               return statusProcessInfo.system;
            }
         }
      }
   }
   return "Windows 7"; // this is a hack
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
   var processSystem = message.system;
   var processSystemTime = message.time;
   var processRunning = message.running == "true";
   
   statusProcesses[process] = {
      resource: processResource,
      system: processSystem,
      time: processSystemTime,
      running: processRunning, // is anything running
      focus: processFocus
   };
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
   var statusInfo = statusProcesses[process];
   
   if(statusInfo != null){
      $("#toolbarDebug").css('opacity', '1.0');
      $("#toolbarDebug").css('filter', 'alpha(opacity=100)'); // msie
      $("#process").html("<i>&nbsp;RUNNING: " + statusInfo.resource + " ("+process+")</i>");
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
         var statusProcessInfo = statusProcesses[statusProcess];
         
         if(statusProcessInfo != null) {
            var displayName = "<div class='debugIdleRecord'>"+statusProcess+"</div>";
            var resourcePath = "";
            var status = "WAITING";
            var running = false;
            
            if(statusProcessInfo.resource != null) {
               var resourcePathDetails = createResourcePath(statusProcessInfo.resource);
               
               if(statusFocus == statusProcess) {
                  displayName = "<div class='debugFocusRecord'>"+statusProcess+"</div>";
                  status = "DEBUGGING";
               } else {
                  displayName = "<div class='debugRecord'>"+statusProcess+"</div>";
                  status = "RUNNING";
               }
               resourcePath = resourcePathDetails.resourcePath;
               running = true;
            }
            statusRecords.push({
               recid: statusIndex++,
               name: displayName,
               process: statusProcess,
               status: status,
               running: running,
               system: statusProcessInfo.system,
               resource: statusProcessInfo.resource,
               focus: statusFocus == statusProcess,
               script: resourcePath
            });
         }
      }
   }
   w2ui['debug'].records = statusRecords;
   w2ui['debug'].refresh();
}

registerModule("debug", "Debug module: debug.js", createStatus, [ "common", "socket", "tree", "threads" ]);