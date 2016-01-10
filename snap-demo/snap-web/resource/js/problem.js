var problemLine = -1;
var problemMessage = null;

function registerProblems() {
	createRoute('Y', updateProblems);
}

function showProblems(){
	var problemsElement = document.getElementById("problems");
	
	if(problemsElement != null) {
	   if(problemMessage != null) {
		  problemsElement.innerHTML = problemMessage;
	   }
	}
}

function clearProblems() {
	var problemsElement = document.getElementById("problems");
	
	if(problemsElement != null) {
		problemsElement.innerHTML = "";
	}
    problemMessage = null;
    clearEditorHighlights();
}

function updateProblems(socket, text) {
   var index = text.indexOf(':');
   var message = text.substring(index + 1);
   var location = text.substring(0, index);
   var line = parseInt(location);
   
   problemLine = line;
   problemMessage = message;
   createEditorHighlight(line, "errorMarker");
}

registerModule("problem", "Problem module: problem.js", registerProblems, ["common", "socket"]);