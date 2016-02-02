var spinnerHiding = false;
var projectBreakpoints = {};

function newScript() {
   resetEditor();
   clearConsole();
   clearProblems();
}

function runScript() {
   saveScriptWithAction(function() {
      var editorData = loadEditor();
      var message = JSON.stringify({
         breakpoints : editorData.breakpoints,
         project : document.title,
         resource : editorData.resource.filePath,
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

function createLayout() {

   // $('#topLayer').spin({ lines: 10, length: 30, width: 20, radius: 40 });

   // -- LAYOUT
   var pstyle = 'background-color: #F5F6F7; overflow: hidden;';
   $('#mainLayout').w2layout({
      name : 'mainLayout',
      padding : 0,
      panels : [ {
         type : 'top',
         size : '35px',
         resizable : false,
         style : pstyle
      }, {
         type : 'left',
         size : '20%',
         resizable : true,
         style : pstyle,
         content : "<div id='explorer'></div>"
      }, {
         type : 'main',
         size : '80%',
         resizable : true,
         style : pstyle
      } , {
         type : 'bottom',
         size : '20px',
         resizable : false,
         style : pstyle,
         content : "<div id='status'></div>"
      } ]
   });

   var pstyle = 'background-color: #F5F6F7; overflow: hidden;';
   $('#topLayout').w2layout(
         {
            name : 'topLayout',
            padding : 0,
            panels : [
                  {
                     type : 'left',
                     size : '60%',
                     style : pstyle,
                     content : "<div class='titleTop'><table><tr>"
                           + "<td>&nbsp;&nbsp;</td>" 
                           + "<td><div class='saveScript' onclick='saveScript()' title='Save Script'></div></td>" 
                           + "<td><div class='newScript' onclick='newScript()' title='New Script'></div></td>"
                           + "<td><div class='deleteScript' onclick='deleteScript()' title='Delete Script'></div></td>"   
                           + "<td><div class='runScript' onclick='runScript()' title='Run Script'></div></td>" 
                           + "<td><div class='stopScript' onclick='stopScript()' title='Stop Script'></div></td>" 
                           + "<td><div class='resumeScript' onclick='resumeScript()' title='Resume Script'></div></td>" 
                           + "<td><div class='stepInScript' onclick='stepInScript()' title='Step In'></div></td>" 
                           + "<td><div class='stepOutScript' onclick='stepOutScript()' title='Step Out'></div></td>" 
                           + "<td><div class='stepOverScript' onclick='stepOverScript()' title='Step Over'></div></td>" 
                           + "</tr></table></div>"
                  }, {
                     type : 'main',
                     size : '20%',
                     style : pstyle,
                     content : "<div class='titleTop'></div>"
                  }, {
                     type : 'right',
                     size : '20%',
                     style : pstyle,
                     content : "<div class='titleTop'></div>"
                  } ]
         });

   var pstyle = 'background-color: #F5F6F7; overflow: hidden;';
   $('#editorLayout').w2layout({
      name : 'editorLayout',
      padding : 0,
      panels : [ {
         type : 'top',
         size : '60%',
         resizable : false,
         style : pstyle,
         content : "<div id='editor'>// TODO write code </div>"
      }, {
         type : 'bottom',
         size : '40%',
         resizable : false,
         style : pstyle + 'border-top: 0px;'
      } ]
   });

   $('').w2layout({
      name : 'tabLayout',
      padding : 0,
      panels : [ {
         type : 'main',
         size : '100%',
         style : pstyle + 'border-top: 0px;',
         resizable : false,
         name : 'tabs',
         tabs : {
            active : 'tab1',
            tabs : [ {
               id : 'tab1',
               caption : '<div class="consoleTab">Console</div>'
            }, {
               id : 'tab2',
               caption : '<div class="errorTab">Problems</div>'
            }, {
               id : 'tab3',
               caption : '<div class="breakpointsTab">Breakpoints</div>'
            }, {
               id : 'tab4',
               caption : '<div class="threadTab">Threads</div>'
            }, {
               id : 'tab5',
               caption : '<div class="variableTab">Variables</div>'
            }, {
               id : 'tab6',
               caption : '<div class="telemetryTab">Telemetry</div>'
            } ],
            onClick : function(event) {
               if (event.target == 'tab1') {
                  w2ui['tabLayout'].content('main', "<div style='overflow: scroll; font-family: monospace;' id='console'></div>");
                  w2ui['tabLayout'].refresh();
                  showConsole();
               } else if (event.target == 'tab2') {
                  w2ui['tabLayout'].content('main', "<div style='overflow: scroll; font-family: monospace;' id='problems'></div>");
                  w2ui['tabLayout'].refresh();
                  $('#problems').w2render('problems');
                  showProblems();
               } else if (event.target == 'tab3') {
                  w2ui['tabLayout'].content('main', "<div style='overflow: scroll; font-family: monospace;' id='breakpoints'></div>");
                  w2ui['tabLayout'].refresh();
                  $('#breakpoints').w2render('breakpoints');
                  showEditorBreakpoints();
               } else if(event.target == 'tab4'){
                  w2ui['tabLayout'].content('main', "<div style='overflow: scroll; font-family: monospace;' id='threads'></div>");
                  w2ui['tabLayout'].refresh();
                  $('#threads').w2render('threads');
                  showThreads();
               } else if(event.target == 'tab5'){
                  w2ui['tabLayout'].content('main', "<div style='overflow: scroll; font-family: monospace;' id='variables'></div>");
                  w2ui['tabLayout'].refresh();
                  $('#variables').w2render('variables');
                  showVariables();
               } else {
                  w2ui['tabLayout'].content('main', "<div style='overflow: scroll; font-family: monospace;' id='telemetry'></div>");
                  w2ui['tabLayout'].refresh();
                  $('#telemetry').w2render('telemetry');
                  showVariables();
               }
            }
         }
      } ]
   });

   $().w2grid({
      name : 'problems',
      columns : [ {
         field : 'description',
         caption : 'Description',
         size : '45%',
         sortable : true,
         resizable : true
      },{
         field : 'location',
         caption : 'Location',
         size : '10%',
         sortable : true,
         resizable : true
      }, {
         field : 'resource',
         caption : 'Resource',
         size : '45%',
         sortable : true,
         resizable : true
      },  ],
      onClick : function(event) {
         var grid = this;
         event.onComplete = function() {
            var sel = grid.getSelection();
            if (sel.length == 1) {
               var record = grid.get(sel[0]);
               openTreeFile(record.script, function(){}); // open resource
            }
         }
      }
   });

   $().w2grid({
      name : 'variables',
      columns : [ {
         field : 'name',
         caption : 'Name',
         size : '30%',
         sortable : false
      }, {
         field : 'value',
         caption : 'Value',
         size : '40%',
         sortable : false
      }, {
         field : 'type',
         caption : 'Type',
         size : '30%'
      } ],
      onClick : function(event) {
         var grid = this;
         event.onComplete = function() {
            var sel = grid.getSelection();
            if (sel.length == 1) {
               var record = grid.get(sel[0]);
               toggleExpandVariable(record.path);
            }
         }
      }
   });
   
   $().w2grid({
      name : 'telemetry',
      columns : [ {
         field : 'resource',
         caption : 'Resource',
         size : '40%',
         sortable : false
      },  {
         field : 'line',
         caption : 'Line',
         size : '20%'
      }, {
         field : 'count',
         caption : 'Count',
         size : '20%'
      }, {
         field : 'duration',
         caption : 'Duration',
         size : '20%',
         sortable : false
      }],
      onClick : function(event) {
         var grid = this;
         event.onComplete = function() {
            var sel = grid.getSelection();
            if (sel.length == 1) {
               var record = grid.get(sel[0]);
               openTreeFile(record.script, function() {
                  showEditorLine(record.line);  
               }); 
            }
         }
      }
   });

   $().w2grid({
      name : 'breakpoints',
      columns : [ 
       {
         field : 'name',
         caption : 'Resource',
         size : '60%',
         sortable : true,
         resizable : true
      },{
         field : 'location',
         caption : 'Location',
         size : '40%',
         sortable : true,
         resizable : true
      } ],
      onClick : function(event) {
         var grid = this;
         event.onComplete = function() {
            var sel = grid.getSelection();
            if (sel.length == 1) {
               var record = grid.get(sel[0]);
               openTreeFile(record.script, function() {
                  showEditorLine(record.line);  
               }); 
            }
         }
      }
   });

   $().w2grid({
      name : 'threads',
      columns : [ {
         field : 'name',
         caption : 'Thread',
         size : '20%',
         sortable : true,
         resizable : true
      }, {
         field : 'status',
         caption : 'Status',
         size : '10%',
         sortable : true,
         resizable : true
      }, {
         field : 'instruction',
         caption : 'Instruction',
         size : '20%',
         sortable : true,
         resizable : true
      },{
         field : 'resource',
         caption : 'Resource',
         size : '30%',
         sortable : true,
         resizable : true
      },{
         field : 'line',
         caption : 'Line',
         size : '10%',
         sortable : true,
         resizable : true
      },{
         field : 'depth',
         caption : 'Depth',
         size : '10%',
         sortable : false,
         resizable : true
      },],
      onClick : function(event) {
         var grid = this;
         event.onComplete = function() {
            var sel = grid.getSelection();
            if (sel.length == 1) {
               var record = grid.get(sel[0]);
               openTreeFile(record.script, function(){
                  updateThreadFocus(record.thread, record.line);
                  showEditorLine(record.line);  
               });
            }
         }
      }
   });
   
   w2ui['mainLayout'].content('top', w2ui['topLayout']);
   w2ui['mainLayout'].content('main', w2ui['editorLayout']);
   w2ui['editorLayout'].content('bottom', w2ui['tabLayout']);

   w2ui['tabLayout'].content('main', "<div style='overflow: scroll; font-family: monospace;' id='console'></div>");
   w2ui['tabLayout'].refresh();

}

registerModule("project", "Project module: project.js", createLayout, [ "common", "socket", "console", "problem", "editor", "spinner", "tree", "threads" ]);
