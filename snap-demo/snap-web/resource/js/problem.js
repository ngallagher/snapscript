var problemLine = -1;
var problemMessage = null;
var problemProject = null;
var problemLocation = null;

function registerProblems() {
	createRoute('SYNTAX_ERROR', updateProblems);
}

function showProblems() {
	var problems = w2ui['problems'];
	
	if(problemMessage != null && problems != null) {
		problems.records = [{ 
		   recid: 1,
		   location: "Line " + problemLine, 
         resource: problemLocation, 
         description: problemMessage, 
         project: problemProject, 
         script: "/resource/" + problemProject + problemLocation
      }];
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

function updateProblems(socket, type, text) {
	var problems = w2ui['problems'];
	var message = JSON.parse(text);
   
   problemLine = message.line;
   problemMessage = message.description;
   problemLocation = message.resource;
   problemProject = message.project;
   createEditorHighlight(problemLine, "errorMarker");
	
	if(problems != null) {
      problems.records = [{
            recid: 1,
            location: "Line " + problemLine, 
            resource: problemLocation, 
            description: problemMessage, 
            project: problemProject, 
            script: "/resource/" + problemProject + problemLocation
      }];
   	problems.refresh();
	}
}

registerModule("problem", "Problem module: problem.js", registerProblems, ["common", "socket"]);