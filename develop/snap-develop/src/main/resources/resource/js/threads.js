var suspendedThreads = {};
var currentFocusThread = null;
var currentFocusLine = -1;
var threadEditorFocus = {};

function createThreads() {
   createRoute("BEGIN", startThreads, clearThreads);
   createRoute("SCOPE", updateThreads, clearVariables);
}

function startThreads(socket, type, text) {
   var message = JSON.parse(text);
   
   suspendedThreads = {};
   threadEditorFocus = {};
   currentFocusThread = null;
   clearVariables();
   clearProfiler();
   clearThreads();
   $("#process").html("<i>&nbsp;RUNNING: " + message.resource + " ("+message.process+") "+message.duration+" milliseconds</i>");
}

function terminateThreads() {
   threadEditorFocus = {};
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
   
   if(hasThreadFocusLineChanged(threadScope)) { // this will keep switching!!
      if(hasThreadFocusResourceChanged(threadScope)) { // e.g /game/tetris.snap
         if(isThreadUpdateNew(threadScope)) { // has this thread been focused before!!
            var resourcePathDetails = createResourcePath(threadScope.resource);
            
            openTreeFile(resourcePathDetails.resourcePath, function(){
               updateThreadFocus(threadScope.thread, threadScope.line, threadScope.key);
               showEditorLine(threadScope.line);
            });
         }
      } else {
         updateThreadFocus(threadScope.thread, threadScope.line, threadScope.key);
         showEditorLine(threadScope.line);
      }
   }
   suspendedThreads[threadScope.thread] = threadScope;
   showThreads();
   showVariables();
} 

function isThreadUpdateNew(threadScope) {
   var threadEditorFocusKey = threadEditorFocus[threadScope.thread]; // get last key for this thread
   return threadEditorFocusKey != threadScope.key; // has this thread been focused before!!
}

function hasThreadFocusLineChanged(threadScope) {
   return currentFocusThread != threadScope.thread || currentFocusLine != threadScope.line; // hash the thread or focus line changed
}

function hasThreadFocusResourceChanged(threadScope) {
   var editorData = loadEditor();
   return editorData.resource.filePath != threadScope.resource; // is there a need to update the editor
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

function updateThreadFocus(thread, line, key) {
   threadEditorFocus[thread] = key; // remember we already focused here
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
            key: threadScope.key,
            line: threadScope.line,
            script: resourcePathDetails.resourcePath
         });
      }
   }
   w2ui['threads'].records = threadRecords;
   w2ui['threads'].refresh();
}

registerModule("threads", "Thread module: threads.js", createThreads, [ "common", "socket", "explorer" ]);