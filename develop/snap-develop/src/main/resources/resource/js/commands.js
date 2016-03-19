
function newFile() {
   newFileTreeDialog(null, true, function(resourceDetails) {
      if(!isResourceFolder(resourceDetails.filePath)) {
         var message = JSON.stringify({
            project : document.title,
            resource : resourceDetails.filePath,
            source : "",
            directory: false,
            create: true
         });
         clearConsole();
         clearProblems();
         socket.send("SAVE:" + message);
         updateEditor("", resourceDetails.projectPath);
      }
   });
}

function newDirectory() {
   newDirectoryTreeDialog(null, true, function(resourceDetails) {
      if(isResourceFolder(resourceDetails.filePath)) {
         var message = JSON.stringify({
            project : document.title,
            resource : resourceDetails.filePath,
            source : "",
            directory: true,
            create: true
         });
         clearConsole();
         clearProblems();
         socket.send("SAVE:" + message);
      }
   });
}

function saveFile() {
   saveFileWithAction(function(){}, true);
}

function saveFileWithAction(saveCallback, update) {
   var editorData = loadEditor();
   if (editorData.resource == null) {
      openTreeDialog(null, false, function(resourceDetails) {
         var message = JSON.stringify({
            project : document.title,
            resource : resourceDetails.filePath,
            source : editorData.source,
            directory: false,
            create: false
         });
         clearConsole();
         clearProblems();
         socket.send("SAVE:" + message);
         
         if(update) { // should editor be updated
            updateEditor(editorData.source, resourceDetails.projectPath);
         }
         saveCallback();
      });
   } else {
      if (isEditorChanged()) {
         openTreeDialog(editorData.resource, true, function(resourceDetails) {
            var message = JSON.stringify({
               project : document.title,
               resource : resourceDetails.filePath,
               source : editorData.source,
               directory: false,
               create: false
            });
            clearConsole();
            clearProblems();
            socket.send("SAVE:" + message); 
         
            if(update) { // should the editor be updated?
               updateEditor(editorData.source, resourceDetails.projectPath);
            }
            saveCallback();
         });
      } else {
         clearConsole();
         clearProblems();
         saveCallback();
      }
   }
}

function deleteFile(resourceDetails) {
   var editorData = loadEditor();
   if(resourceDetails == null && editorData.resource != null) {
      resourceDetails = editorData.resource;
   }
   if(resourceDetails != null) {
      var message = JSON.stringify({
         project : document.title,
         resource : resourceDetails.filePath
      });
      clearConsole();
      clearProblems();
      socket.send("DELETE:" + message);
      
      if(editorData.resource != null && editorData.resource.resourcePath == resourceDetails.resourcePath) { // delete focused file
         resetEditor();
      }
   }
}

function deleteDirectory(resourceDetails) {
   if(resourceDetails != null) {
      var message = JSON.stringify({
         project : document.title,
         resource : resourceDetails.filePath
      });
      clearConsole();
      clearProblems();
      socket.send("DELETE:" + message);
   }
}

function runScript() {
   var waitingProcessSystem = findStatusWaitingProcessSystem();
   saveFileWithAction(function() {
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
   var editorData = loadEditor();
   var message = JSON.stringify({
      process: process,
      breakpoints : editorData.breakpoints,
      project : document.title,
      focus: statusFocus != process // toggle the focus
   });
   socket.send("ATTACH:" + message); // attach to process
}

function switchProject() {
   document.location="/";
}

registerModule("commands", "Commands module: commands.js", null, [ "common", "editor", "tree", "threads" ]);