function createEditor() {
    window.setTimeout(showEditor, 1000);
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

registerModule("editor", "Grid module: editor.js", createEditor, ["common", "spinner"]);