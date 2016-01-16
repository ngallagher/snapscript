var suspendedThreads = {};
var currentFocusThread = null;

function createThreads() {
   createRoute("SCOPE", updateThreads);
   createRoute("EXIT", clearThreads);
}

function clearThreads(socket, type, text) {
   suspendedThreads = {};
   currentFocusThread = null;
   w2ui['threads'].records = [];
   w2ui['threads'].refresh();
}

function updateThreads(socket, type, text) {
   var scope = JSON.parse(text);
   suspendedThreads[scope.thread] = scope;
}

function focusedThread() {
   return currentFocusThread;
}

function clearFocusThread() {
   currentFocusThread = null;
}

function updateThreadFocus(thread) {
   currentFocusThread = thread;
} 

function focusedThreadVariables() {
   if(currentFocusThread != null) {
      var suspendedThread = suspendedThreads[currentFocusThread];
      
      if(suspendedThread != null) {
         return suspendedThread.variables;
      }
   }
   return {};
}

function showThreads() {
   var threadRecords = [];
   var threadIndex = 1;
   
   for (var threadName in suspendedThreads) {
      if (suspendedThreads.hasOwnProperty(threadName)) {
         var threadScope = suspendedThreads[threadName];

         threadRecords.push({
            recid: threadIndex++,
            thread: threadName,
            status: 'SUSPENDED',
            variables: threadScope.variables,
            resource: threadScope.resource,
            line: threadScope.line,
            script: buildTreeFile(threadScope.resource)
         });
      }
   }
   w2ui['threads'].records = threadRecords;
   w2ui['threads'].refresh();
}

registerModule("threads", "Thread module: threads.js", createThreads, [ "common", "socket", "tree" ]);