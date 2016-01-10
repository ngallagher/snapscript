var problemLine = -1;
var problemMessage = null;
var problemLocation = null;

function registerProblems() {
	createRoute('Y', updateProblems);
}

function showProblems() {
	var problems = w2ui['problems'];
	
	if(problemMessage != null && problems != null) {
		problems.records = [{location: "Line " + problemLine, problemLocation: location, description: problemMessage}];
		problems.refresh();
	}
}

function clearProblems() {
	var problems = w2ui['problems'];
	
	problemMessage = null;
   problemLocation = null;
   clearEditorHighlights();
    
	if(problems != null) {
	    problems.records = [];
	    problems.refresh();
	}
}

function updateProblems(socket, text) {
	var problems = w2ui['problems'];
	var index = text.indexOf(':');
   var message = text.substring(index + 1);
   var location = text.substring(0, index);
   var line = parseInt(location);
   
   problemLine = line;
   problemMessage = message;
   problemLocation = location;
   createEditorHighlight(line, "errorMarker");
	
	if(problems != null) {
        problems.records = [{location: "Line " + line, resource: location, description: message}];
   		problems.refresh();
	}
}

registerModule("problem", "Problem module: problem.js", registerProblems, ["common", "socket"]);