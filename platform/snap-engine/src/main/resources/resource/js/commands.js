
function newScript() {
   resetEditor();
   clearConsole();
   clearProblems();
}

function runScript() {
   var waitingProcessSystem = findStatusWaitingProcessSystem();
   saveScriptWithAction(function() {
      var editorData = loadEditor();
      var message = JSON.stringify({
         breakpoints : editorData.breakpoints,
         project : document.title,
         resource : editorData.resource.filePath,
         system: waitingProcessSystem,
         source : editorData.source,
      });
      socket.send("EXECUTE:" + message);
   });
}

function saveScript() {
   saveScriptWithAction(function() {
   });
}

function saveScriptWithAction(saveCallback) {
   var editorData = loadEditor();
   if (editorData.resource == null) {
      openTreeDialog(null, false, function(resourceDetails) {
         var message = JSON.stringify({
            project : document.title,
            resource : resourceDetails.filePath,
            source : editorData.source,
         });
         clearConsole();
         clearProblems();
         socket.send("SAVE:" + message);
         updateEditor(editorData.source, resourceDetails.projectPath);
         saveCallback();
      });
   } else {
      if (isEditorChanged()) {
         openTreeDialog(editorData.resource, true, function(resourceDetails) {
            var message = JSON.stringify({
               project : document.title,
               resource : resourceDetails.filePath,
               source : editorData.source,
            });
            clearConsole();
            clearProblems();
            socket.send("SAVE:" + message); 
            updateEditor(editorData.source, resourceDetails.projectPath);
            saveCallback();
         });
      } else {
         clearConsole();
         clearProblems();
         saveCallback();
      }
   }
}

function deleteScript() {
   var editorData = loadEditor();
   var message = JSON.stringify({
      project : document.title,
      resource : editorData.resource.filePath
   });
   clearConsole();
   clearProblems();
   socket.send("DELETE:" + message);
}

function updateScriptBreakpoints() {
   var editorData = loadEditor();
   var message = JSON.stringify({
      breakpoints : editorData.breakpoints,
      project : document.title,
   });
   socket.send("BREAKPOINTS:" + message);
}

function stepOverScript() {
   var threadScope = focusedThread();
   if(threadScope != null) {
      var message = JSON.stringify({
         thread: threadScope.thread,
         type: "STEP_OVER"
      });
      clearEditorHighlights() 
      socket.send("STEP:" + message);
   }
}

function stepInScript() {
   var threadScope = focusedThread();
   if(threadScope != null) {
      var message = JSON.stringify({
         thread: threadScope.thread,
         type: "STEP_IN"
      });
      clearEditorHighlights() 
      socket.send("STEP:" + message);
   }
}

function stepOutScript() {
   var threadScope = focusedThread();
   if(threadScope != null) {
      var message = JSON.stringify({
         thread: threadScope.thread,
         type: "STEP_OUT"
      });
      clearEditorHighlights() 
      socket.send("STEP:" + message);
   }
}

function resumeScript() {
   var threadScope = focusedThread();
   if(threadScope != null) {
      var message = JSON.stringify({
         thread: threadScope.thread,
         type: "RUN"
      });
      clearEditorHighlights() 
      socket.send("STEP:" + message);
   }
}

function stopScript() {
   socket.send("STOP");
}

function browseScriptVariables(variables) {
   var threadScope = focusedThread();
   if(threadScope != null) {
      var message = JSON.stringify({
         thread: threadScope.thread,
         expand: variables
      });
      socket.send("BROWSE:" + message);
   }
}

function attachProcess(process) {
   var statusFocus = currentStatusFocus(); // what is the current focus
   var message = JSON.stringify({
      process: process,
      focus: statusFocus != process // toggle the focus
   });
   socket.send("ATTACH:" + message); // attach to process
}