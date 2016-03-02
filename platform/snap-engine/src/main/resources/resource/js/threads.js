var suspendedThreads = {};
var currentFocusThread = null;
var currentFocusLine = -1;

function createThreads() {
   createRoute("BEGIN", startThreads, clearThreads);
   createRoute("SCOPE", updateThreads, clearVariables);
}

function startThreads(socket, type, text) {
   var message = JSON.parse(text);
   
   suspendedThreads = {};
   currentFocusThread = null;
   clearVariables();
   clearProfiler();
   clearThreads();
   $("#process").html("<i>&nbsp;RUNNING: " + message.resource + " ("+message.process+") "+message.duration+" milliseconds</i>");
}

function terminateThreads() {
   suspendedThreads = {};
   currentFocusThread = null;
   clearEditorHighlights(); // this should be done in editor.js, i.e createRoute("EXIT" ... )
   clearVariables();
   clearThreads();
}

function clearThreads() {
   w2ui['threads'].records = [];
   w2ui['threads'].refresh();
   $("#process").html("");
}

function updateThreads(socket, type, text) {
   var threadScope = JSON.parse(text);
   var editorData = loadEditor();
   
   if(currentFocusThread != threadScope.thread || currentFocusLine != threadScope.line) { // this will keep switching!!
      if(editorData.resource.filePath != threadScope.resource) { // e.g /game/tetris.snap
         var resourcePathDetails = createResourcePath(threadScope.resource);
         
         openTreeFile(resourcePathDetails.resourcePath, function(){
            updateThreadFocus(threadScope.thread, threadScope.line);
            showEditorLine(threadScope.line);
         });
      } else {
         updateThreadFocus(threadScope.thread, threadScope.line);
         showEditorLine(threadScope.line);
      }
   }
   suspendedThreads[threadScope.thread] = threadScope;
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
         
         if(editorData.resource.filePath == threadScope.resource && threadScope.status == 'SUSPENDED') {
            createEditorHighlight(threadScope.line, "threadHighlight");
         }
         if(threadScope.status != 'SUSPENDED') {
            displayStyle = 'threadRunning';
         }
         var displayName = "<div class='"+displayStyle+"'>"+threadName+"</div>";
         var resourcePathDetails = createResourcePath(threadScope.resource);
         
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
            script: resourcePathDetails.resourcePath
         });
      }
   }
   w2ui['threads'].records = threadRecords;
   w2ui['threads'].refresh();
}

registerModule("threads", "Thread module: threads.js", createThreads, [ "common", "socket", "tree" ]);