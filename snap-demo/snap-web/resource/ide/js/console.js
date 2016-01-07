
function registerConsole() {
	createRoute('C', updateConsole);
}

function clearConsole() {
    document.getElementById("console").innerHTML = ""; 
}

function updateConsole(socket, text) {
	var index = text.indexOf(':');
	var value = text.substring(index + 1);
	var type = text.substring(0, index);
	
	if(type == 'PRINT_ERROR') {
		document.getElementById("console").innerHTML += "<span style='color: red;'>" + value + "</span>"; 
	} else if(type == 'PRINT_OUTPUT'){
	    document.getElementById("console").innerHTML += "<span style='color: black;'>" + value + "</span>"; 
	} 
}

registerModule("console", "Console module: console.js", registerConsole, ["common", "socket"]);