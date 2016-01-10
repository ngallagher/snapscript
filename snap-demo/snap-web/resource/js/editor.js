var editorBreakpoints = {};
var editorResource = null;

function createEditor() {
    window.setTimeout(showEditor, 1000);
}

function clearEditorHighlights() {
	var editor = ace.edit("editor");
	var session = editor.getSession();
	
	for(var editorMarker in session.$backMarkers) {
		session.removeMarker(editorMarker);
	}
}

function createEditorHighlight(line, css){
	var editor = ace.edit("editor");
	var Range = ace.require('ace/range').Range;
	//editor.session.addMarker(new Range(from, 0, to, 1), "errorMarker", "fullLine");
	editor.session.addMarker(new Range(line-1, 0, line-1, 1), css, "fullLine");
}


function clearEditorBreakpoints(row) {
	var editor = ace.edit("editor");
	var session = editor.getSession();
    var breakpoints = session.getBreakpoints();
    var remove = false;
    
    for(var breakpoint in breakpoints) {
    	session.clearBreakpoint(row);
    }
}

function showEditorBreakpoints(){
	var breakpointRecords = [];
	var breakpointIndex = 1;
	for(var resourceName in editorBreakpoints) {
		if(editorBreakpoints.hasOwnProperty(resourceName)) {
			var breakpoints = editorBreakpoints[resourceName];
			
			for(var lineNumber in breakpoints) {
				if(breakpoints.hasOwnProperty(lineNumber)) {
				   if(breakpoints[lineNumber] == true) {
					   breakpointRecords.push({recid: breakpointIndex++, location: "Line " + lineNumber, resource: resourceName});
				   }
				}
			}
		}
	}
	w2ui['breakpoints'].records = breakpointRecords;
	w2ui['breakpoints'].refresh();
}

function setEditorBreakpoint(row, value) {
	if(editorResource != null) {
		var editor = ace.edit("editor");
		var session = editor.getSession();
		var resourceBreakpoints = editorBreakpoints[editorResource];

		if(value) {
			session.setBreakpoint(row);
		}else {
			session.clearBreakpoint(row);
		}
		if(resourceBreakpoints == null){
			resourceBreakpoints = {};
			editorBreakpoints[editorResource] = resourceBreakpoints;
		}
		resourceBreakpoints[row] = value;
	}
	showEditorBreakpoints();
}

function toggleEditorBreakpoint(row) {
	if(editorResource != null) {
		var editor = ace.edit("editor");
		var session = editor.getSession();
		var resourceBreakpoints = editorBreakpoints[editorResource];
		var breakpoints = session.getBreakpoints();
		var remove = false;
		
		for(var breakpoint in breakpoints) {
			if(breakpoint == row) {
				remove = true;
				break;
			}
		}
		if(remove) {
			session.clearBreakpoint(row);
		} else {
			session.setBreakpoint(row);
		}
		if(resourceBreakpoints == null){
			resourceBreakpoints = {};
			resourceBreakpoints[row] = true;
			editorBreakpoints[editorResource] = resourceBreakpoints;
		}else {
			if(resourceBreakpoints[row] == true) {
				resourceBreakpoints[row] = false;
			} else {
				resourceBreakpoints[row] = true;
			}
		}
	}
	showEditorBreakpoints();
}

function clearEditor() {
	var editor = ace.edit("editor");
	var session = editor.getSession();
	
	for(var editorMarker in session.$backMarkers) {
		session.removeMarker(editorMarker);
	}	
    var breakpoints = session.getBreakpoints();
    var remove = false;
    
    for(var breakpoint in breakpoints) {
    	session.clearBreakpoint(breakpoint);
    }
}

function loadEditor(){
	var editor = ace.edit("editor");
	var text = editor.getValue();
	var path = editorResource;
	if(path != null) {
		if(path.startsWith("/resource")) {
			var segments = path.split("/");
			path = "";
			for(var i = 3; i < segments.length; i++) {
				path += "/" + segments[i];
			}
		}
	}
	return {
		breakpoints: editorBreakpoints,
		resource: path,
		source: text
	};
}

function updateEditor(text, resource) {
	var editor = ace.edit("editor");
	editor.setValue(text, 1);
	
	clearEditor();
	clearProblems();
    scrollEditorToTop();
    editorResource = resource;
    
    if(resource != null) {
		var breakpoints = editorBreakpoints[resource];
		if(breakpoints != null) {
			for(var lineNumber in breakpoints) {
				if(breakpoints.hasOwnProperty(lineNumber)) {
				   if(breakpoints[lineNumber] == true) {
					  setEditorBreakpoint(lineNumber, true);
				   }
				}
			}
		}
    }
}

function scrollEditorToTop(){
	var editor = ace.edit("editor");
	var session = editor.getSession();
	session.setScrollTop(0);
}

function showEditor() {
	var editor = ace.edit("editor");
    //editor.setTheme("ace/theme/monokai");
    editor.getSession().setMode("ace/mode/snapscript");	
    editor.getSession().setTabSize(3);
    editor.getSession().setUseSoftTabs(true);
    editor.setShowPrintMargin(false);
    editor.commands.addCommand({
        name: 'run',
        bindKey: {win: 'Ctrl-R',  mac: 'Command-R'},
        exec: function(editor) {
        	runScript();
        },
        readOnly: true // false if this command should not apply in readOnly mode
    });
    editor.on("guttermousedown", function(e){ 
        var target = e.domEvent.target; 
        if (target.className.indexOf("ace_gutter-cell") == -1)  {
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

registerModule("editor", "Editor module: editor.js", createEditor, ["common", "spinner"]);