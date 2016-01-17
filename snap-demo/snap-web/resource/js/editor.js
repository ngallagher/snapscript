var editorBreakpoints = {}; // spans multiple resources
var editorMarkers = {};
var editorResource = null;
var editorText = null;

function createEditor() {
   window.setTimeout(showEditor, 1000);
}

function clearEditorHighlights() {
   var editor = ace.edit("editor");
   var session = editor.getSession();

   for ( var editorLine in editorMarkers) {
      if (editorMarkers.hasOwnProperty(editorLine)) {
         var marker = editorMarkers[editorLine];
         
         if(marker != null) {
            session.removeMarker(marker);
         }
      }
   }
   editorMarkers = {};
}

function showEditorLine(line) {
   var editor = ace.edit("editor");
   
   editor.resize(true);
   
   if(line > 1) {
      editor.scrollToLine(line - 1, true, true, function () {})
   } else {
      editor.scrollToLine(0, true, true, function () {})
   }
}

function clearEditorHighlight(line) {
   var editor = ace.edit("editor");
   var session = editor.getSession();
   var marker = editorMarkers[line];
   
   if(marker != null) {
      session.removeMarker(marker);
   }
}

function createEditorHighlight(line, css) {
   var editor = ace.edit("editor");
   var Range = ace.require('ace/range').Range;
   var session = editor.getSession();

   clearEditorHighlight(line);
   // session.addMarker(new Range(from, 0, to, 1), "errorMarker", "fullLine");
   var marker = session.addMarker(new Range(line - 1, 0, line - 1, 1), css, "fullLine");
   editorMarkers[line] = marker;
}

function clearEditorBreakpoint(row) {
   var editor = ace.edit("editor");
   var session = editor.getSession();
   var breakpoints = session.getBreakpoints();
   var remove = false;

   for ( var breakpoint in breakpoints) {
      session.clearBreakpoint(row);
   }
   showEditorBreakpoints();
}

function showEditorBreakpoints() {
   var breakpointRecords = [];
   var breakpointIndex = 1;

   for ( var resourceName in editorBreakpoints) {
      if (editorBreakpoints.hasOwnProperty(resourceName)) {
         var breakpoints = editorBreakpoints[resourceName];

         for ( var lineNumber in breakpoints) {
            if (breakpoints.hasOwnProperty(lineNumber)) {
               if (breakpoints[lineNumber] == true) {
                  var displayName = "<div class='breakpointEnabled'>"+resourceName+"</div>";
                  
                  breakpointRecords.push({
                     recid: breakpointIndex++,
                     name: displayName,
                     location : "Line " + lineNumber,
                     resource : resourceName,
                     line: parseInt(lineNumber),
                     script : buildTreeFile(resourceName)
                  });
               }
            }
         }
      }
   }
   w2ui['breakpoints'].records = breakpointRecords;
   w2ui['breakpoints'].refresh();
   updateScriptBreakpoints(); // update the breakpoints
}

function setEditorBreakpoint(row, value) {
   if (editorResource != null) {
      var editor = ace.edit("editor");
      var session = editor.getSession();
      var resourceBreakpoints = editorBreakpoints[editorResource];
      var line = parseInt(row);

      if (value) {
         session.setBreakpoint(line);
      } else {
         session.clearBreakpoint(line);
      }
      if (resourceBreakpoints == null) {
         resourceBreakpoints = {};
         editorBreakpoints[editorResource] = resourceBreakpoints;
      }
      resourceBreakpoints[line + 1] = value;
   }
   showEditorBreakpoints();
}

function toggleEditorBreakpoint(row) {
   if (editorResource != null) {
      var editor = ace.edit("editor");
      var session = editor.getSession();
      var resourceBreakpoints = editorBreakpoints[editorResource];
      var breakpoints = session.getBreakpoints();
      var remove = false;

      for ( var breakpoint in breakpoints) {
         if (breakpoint == row) {
            remove = true;
            break;
         }
      }
      if (remove) {
         session.clearBreakpoint(row);
      } else {
         session.setBreakpoint(row);
      }
      var line = parseInt(row);

      if (resourceBreakpoints == null) {
         resourceBreakpoints = {};
         resourceBreakpoints[line + 1] = true;
         editorBreakpoints[editorResource] = resourceBreakpoints;
      } else {
         if (resourceBreakpoints[line + 1] == true) {
            resourceBreakpoints[line + 1] = false;
         } else {
            resourceBreakpoints[line + 1] = true;
         }
      }
   }
   showEditorBreakpoints();
}

function resetEditor() {
   var editor = ace.edit("editor");
   var session = editor.getSession();

   editorMarkers = {};
   editorResource = null;
   editorText = "// TODO write code";
   session.setValue(editorText, 1);
}

function clearEditor() {
   var editor = ace.edit("editor");
   var session = editor.getSession();

   for ( var editorMarker in session.$backMarkers) {
      session.removeMarker(editorMarker);
   }
   var breakpoints = session.getBreakpoints();
   var remove = false;

   for ( var breakpoint in breakpoints) {
      session.clearBreakpoint(breakpoint);
   }
}

function loadEditor() {
   var editor = ace.edit("editor");
   var text = editor.getValue();
   var path = editorResource;
   
   if (path != null) {
      path = extractTreePath(path);
   }
   return {
      breakpoints : editorBreakpoints,
      resource : path,
      source : text
   };
}

function updateEditor(text, resource) {
   var editor = ace.edit("editor");
   editor.setValue(text, 1);

   clearEditor();
   clearProblems();
   scrollEditorToTop();
   editorResource = extractTreePath(resource);
   editorMarkers = {};
   editorText = text;

   if (resource != null) {
      var breakpoints = editorBreakpoints[editorResource];

      if (breakpoints != null) {
         for ( var lineNumber in breakpoints) {
            if (breakpoints.hasOwnProperty(lineNumber)) {
               if (breakpoints[lineNumber] == true) {
                  setEditorBreakpoint(lineNumber - 1, true);
               }
            }
         }
      }
   }
}

function isEditorChanged() {
   var editor = ace.edit("editor");
   var text = editor.getValue();

   return text != editorText;
}

function scrollEditorToTop() {
   var editor = ace.edit("editor");
   var session = editor.getSession();
   session.setScrollTop(0);
}

function showEditor() {
   var editor = ace.edit("editor");
   // editor.setTheme("ace/theme/monokai");
   editor.getSession().setMode("ace/mode/snapscript");
   editor.getSession().setTabSize(3);
   editor.getSession().setUseSoftTabs(true);
   editor.setShowPrintMargin(false);
   editor.commands.addCommand({
      name : 'run',
      bindKey : {
         win : 'Ctrl-R',
         mac : 'Command-R'
      },
      exec : function(editor) {
         runScript();
      },
      readOnly : true
   // false if this command should not apply in readOnly mode
   });
   editor.on("guttermousedown", function(e) {
      var target = e.domEvent.target;
      if (target.className.indexOf("ace_gutter-cell") == -1) {
         return;
      }
      if (!editor.isFocused()) {
         return;
      }
      if (e.clientX > 25 + target.getBoundingClientRect().left) {
         return;
      }
      var row = e.getDocumentPosition().row;
      // should be a getBreakpoints but does not seem to be there!!
      toggleEditorBreakpoint(row);
      e.stop()
   });
   scrollEditorToTop();
   finishedLoading();
}

registerModule("editor", "Editor module: editor.js", createEditor, [ "common", "spinner" ]);