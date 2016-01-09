var consoleWindow = [];
var consoleUpdate = false;
var consoleCapacity = 1000;

function registerConsole() {
	createRoute('C', updateConsole);
	setInterval(showConsole, 200); // prevents reflow overload when console is busy
}

function clearConsole() {
	var consoleElement = document.getElementById("console");
	
	if(consoleElement != null) {
       document.getElementById("console").innerHTML = "";
	}
    consoleWindow = [];
}

function showConsole() {
	var consoleElement = document.getElementById("console");
	var consoleText = null;
	var previous = null;
	
	if(consoleElement != null && consoleUpdate) {
		consoleUpdate = false;
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
		if(consoleText != null) {
			consoleText += "</span>";
			consoleElement.innerHTML = consoleText;
			consoleElement.scrollTop = consoleElement.scrollHeight;
		}
	}
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
	consoleUpdate = true;
}

registerModule("console", "Console module: console.js", registerConsole, ["common", "socket"]);