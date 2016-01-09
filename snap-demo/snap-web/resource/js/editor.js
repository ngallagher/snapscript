function createEditor() {
    window.setTimeout(showEditor, 1000);
}

function highlightEditorLine(line){
	var editor = ace.edit("editor");
	var Range = ace.require('ace/range').Range;
	//editor.session.addMarker(new Range(from, 0, to, 1), "errorMarker", "fullLine");
	editor.session.addMarker(new Range(line, 0, line, 1), "errorMarker", "fullLine");
}

function loadEditor(){
	var editor = ace.edit("editor");
	return editor.getValue();
}

function updateEditor(text) {
	var editor = ace.edit("editor");
	editor.setValue(text, 1);
	highlightEditorLine(2);//test
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
    finishedLoading();
}

registerModule("editor", "Editor module: editor.js", createEditor, ["common", "spinner"]);