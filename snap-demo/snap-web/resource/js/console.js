var consoleWindow = [];
var consoleCapacity = 1000;

function registerConsole() {
	createRoute('C', updateConsole);
}

function clearConsole() {
    document.getElementById("console").innerHTML = ""; 
    consoleWindow = [];
}

function updateConsole(socket, text) {
	var index = text.indexOf(':');
	var value = text.substring(index + 1);
	var type = text.substring(0, index);
	var node = {
		error: type == 'PRINT_ERROR',
		text: value,
	};
	consoleWindow.push(node); // put at the end, i.e index consoleWindow.length - 1
	
	if(consoleWindow.length > consoleCapacity) {	
		consoleWindow.shift(); // remove from the start, i.e index 0
	}
	var consoleText = null;
	var previous = null;
	
	for(var i = 0; i < consoleWindow.length; i++) {
		var next = consoleWindow[i];
		
		if(previous == null) {
			if(next.error) {
				consoleText = "<span class='consoleError'>" + next.text;
			} else {
				consoleText = "<span class='consoleNormal'>" + next.text;
			}
			previous = next.error;
		} else if(next.error != previous) {
			consoleText += "</span>";
			
			if(next.error) {
				consoleText += "<span class='consoleError'>" + next.text;
			} else {
				consoleText += "<span class='consoleNormal'>" + next.text;
			}
			previous = next.error;
		} else {
			consoleText += next.text;
		}
	}
	var consoleElement = document.getElementById("console");
	
	consoleText += "</span>";
	consoleElement.innerHTML = consoleText;
	consoleElement.scrollTop = consoleElement.scrollHeight;
	
}

registerModule("console", "Console module: console.js", registerConsole, ["common", "socket"]);