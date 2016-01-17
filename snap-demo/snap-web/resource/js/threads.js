var suspendedThreads = {};
var currentFocusThread = null;
var currentFocusLine = -1;

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
   var editorData = loadEditor();
   
   if(currentFocusThread != scope.thread || currentFocusLine != scope.line) { // this will keep switching!!
      if(editorData.resource != scope.resource) {
         var treeFile = buildTreeFile(scope.resource);
         openTreeFile(treeFile, function(){
            updateThreadFocus(scope.thread, scope.line);
            showEditorLine(scope.line);
         });
      } else {
         updateThreadFocus(scope.thread, scope.line);
         showEditorLine(scope.line);
      }
   }
   suspendedThreads[scope.thread] = scope;
   showThreads();
   showVariables();
} 

function focusedThread() {
   if(currentFocusThread != null) {
      return suspendedThreads[currentFocusThread];
   }
   return null;
}

function clearFocusThread() {
   currentFocusThread = null;
   currentFocusLine = -1;
}

function updateThreadFocus(thread, line) {
   currentFocusThread = thread;
   currentFocusLine = line;
} 

function focusedThreadVariables() {
   if(currentFocusThread != null) {
      var threadScope = suspendedThreads[currentFocusThread];
      
      if(threadScope != null) {
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
         var displayStyle = 'threadSuspended';
         
         if(editorData.resource == threadScope.resource && threadScope.status == 'SUSPENDED') {
            createEditorHighlight(threadScope.line, "threadHighlight");
         }
         if(threadScope.status != 'SUSPENDED') {
            displayStyle = 'threadRunning';
         }
         var displayName = "<div class='"+displayStyle+"'>"+threadName+"</div>";
         
         threadRecords.push({
            recid: threadIndex++,
            name: displayName,
            thread: threadName,
            status: threadScope.status,
            depth: threadScope.depth,
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