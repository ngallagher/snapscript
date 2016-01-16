var suspendedThreads = {};
var suspendedThreadStatus = {};
var currentFocusThread = null;

function createThreads() {
   createRoute("START", startThreads)
   createRoute("SCOPE", updateThreads);
   createRoute("TERMINATE", clearThreads);
   createRoute("EXIT", clearThreads);
}

function startThreads(socket, type, text) {
   var message = JSON.parse(text);
   
   suspendedThreads = {};
   currentFocusThread = null;
   w2ui['threads'].records = [];
   w2ui['threads'].refresh();
   $("#status").html("<i>RUNNING: " + message.resource + " ("+message.process+")</i>");
}

function clearThreads(socket, type, text) {
   suspendedThreads = {};
   currentFocusThread = null;
   w2ui['threads'].records = [];
   w2ui['threads'].refresh();
   $("#status").html("");
}

function updateThreads(socket, type, text) {
   var scope = JSON.parse(text);
   
   suspendedThreadStatus[scope.thread] = 'SUSPENDED';
   suspendedThreads[scope.thread] = scope;
   showThreads();
}

function focusedThreadResume() {
   if(currentFocusThread != null) {
      suspendedThreadStatus[currentFocusThread] = 'RUNNING';
   }
} 

function focusedThread() {
   if(currentFocusThread != null) {
      return suspendedThreads[currentFocusThread];
   }
   return null;
}

function clearFocusThread() {
   currentFocusThread = null;
}

function updateThreadFocus(thread) {
   currentFocusThread = thread;
} 

function focusedThreadVariables() {
   if(currentFocusThread != null) {
      var threadScope = suspendedThreads[currentFocusThread];
      var threadStatus = suspendedThreadStatus[currentFocusThread];
      
      if(threadStatus == 'SUSPENDED') {
         return threadScope.variables;
      }
   }
   return {};
}

function showThreads() {
   var editorData = loadEditor();
   var threadRecords = [];
   var threadIndex = 1;
   
   for (var threadName in suspendedThreads) {
      if (suspendedThreads.hasOwnProperty(threadName)) {
         var threadScope = suspendedThreads[threadName];
         var threadStatus = suspendedThreadStatus[threadName];
         
         if(threadStatus == null) {
            threadStatus = 'SUSPENDED';
         }
         if(editorData.resource == threadScope.resource) {
            createEditorHighlight(threadScope.line, "threadHighlight");
         }
         threadRecords.push({
            recid: threadIndex++,
            thread: threadName,
            status: threadStatus,
            instruction: threadScope.instruction,
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