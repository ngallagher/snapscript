var spinnerHiding = false;
var projectBreakpoints = {};

function runScript(){
   //reconnect("best");
	var editorData = loadEditor();
	var message = JSON.stringify({
		breakpoints: editorData.breakpoints,
		project: document.title,
		resource: editorData.resource,
		source: editorData.source,
	});
	clearConsole();
	clearProblems();
	socket.send("execute:"+ message);
}

function saveScript(){
   //reconnect("company");
	var editorData = loadEditor();
	var message = JSON.stringify({
		project: document.title,
		resource: editorData.resource,
		source: editorData.source,
	});
	clearConsole();
	clearProblems();
	socket.send("save:"+message);
}

function deleteScript(){
   //reconnect("company");
	var text = loadEditor();
	clearConsole();
	clearProblems();
	socket.send("delete:"+text);
}

function suspendScript(resource, line){
   //reconnect("company");

	var editorData = loadEditor();
	var message = JSON.stringify({
		breakpoints: editorData.breakpoints,
		project: document.title,
		resource: editorData.resource
	});
	socket.send("suspend:"+message);
}

function stopScript() {
   //disableRoutes();
}

function createLayout() {
    
    //  $('#topLayer').spin({ lines: 10, length: 30, width: 20, radius: 40 }); 
      
    // -- LAYOUT
    var pstyle = 'background-color: #F5F6F7; overflow: hidden;';
    $('#mainLayout').w2layout({
        name: 'mainLayout',
        padding: 0,
        panels: [
            { type: 'top', size: '40px', style: pstyle },          
            { type: 'left', size: '20%', resizable: true, style: pstyle, content: "<div id='explorer'></div>" },        
            { type: 'main', size: '80%', resizable: true, style: pstyle }           
        ]
    });
    
    var pstyle = 'background-color: #F5F6F7; overflow: hidden;';
    $('#topLayout').w2layout({
        name: 'topLayout',
        padding: 0,
        panels: [                      
            { type: 'left', size: '40%', style: pstyle, content: "<div class='titleTop'>" +
					"<table>" +
					"<tr>" +
					"<td>" +
					"<button id='runScript' class='btn' onclick='runScript()'>Run</button>" +
					"</td>" +
					"<td>" +
					"<button id='stopScript' class='btn' onclick='stopScript()'>Stop</button>" +
					"</td>" +
					"<td>" +
					"<button id='saveScript' class='btn' onclick='saveScript()'>Save</button>" +
					"</td>" +
					"</tr>" +
					"</table>" +
					"</div>" },        
            { type: 'main', size: '30%', style: pstyle, content: "<div class='titleTop'></div>" },
            { type: 'right', size: '30%', style: pstyle, content: "<div class='titleTop'></div>" }            
        ]
    });    

    var pstyle = 'background-color: #F5F6F7; overflow: hidden;';
    $('#blueLayout').w2layout({
        name: 'blueLayout',
        padding: 0,
        panels: [
            { type: 'top', size: '60%', resizable: false, style: pstyle, content: "<div id='editor'>// TODO write code </div>" },        
            { type: 'bottom', size: '40%', resizable: false, style: pstyle + 'border-top: 0px;'}
        ]
    }); 
    
    $('').w2layout({
        name: 'tabLayout',
        padding: 0,
        panels: [
            { type: 'main', size: '100%', style: pstyle + 'border-top: 0px;', resizable: false,
                name: 'tabs',
                tabs: {
                    active: 'tab1',
                    tabs: [
                        { id: 'tab1', caption: 'Console' },
                        { id: 'tab2', caption: 'Problems' },
                        { id: 'tab3', caption: 'Breakpoints' },
                        { id: 'tab4', caption: 'Variables' }
                    ],
                    onClick: function (event) {
                        if(event.target == 'tab1') {
                           w2ui['tabLayout'].content('main', "<div style='overflow: scroll; font-family: monospace;' id='console'></div>");
                           w2ui['tabLayout'].refresh();
                           showConsole();
                        } else if(event.target == 'tab2') {
                           w2ui['tabLayout'].content('main', "<div style='overflow: scroll; font-family: monospace;' id='problems'></div>");   
                           w2ui['tabLayout'].refresh();
                           $('#problems').w2render('problems');
                           showProblems();
                        }else if(event.target == 'tab3') {
                           w2ui['tabLayout'].content('main', "<div style='overflow: scroll; font-family: monospace;' id='breakpoints'></div>");   
                           w2ui['tabLayout'].refresh();
                           $('#breakpoints').w2render('breakpoints');
                           showEditorBreakpoints();
                        }else {
                           w2ui['tabLayout'].content('main', "<div style='overflow: scroll; font-family: monospace;' id='variables'></div>");   
                           w2ui['tabLayout'].refresh();
                           $('#variables').w2render('variables');
                           //showVariables();
                        }
                    }
                }
            }
        ]
    });
    
    var problemRecords = [
    	{ recid: 1, location: 'Line 1', resource: '/io.snap', description: 'Syntax error' },
    	{ recid: 2, location: 'Line 237', resource: '/math/Math.snap', description: 'Syntax error' },
    	{ recid: 3, location: 'Line 44', resource: '/loops.snap', description: 'Syntax error' }
	];
  
    var breakpointRecords = [
      	{ recid: 1, location: 'Line 1', resource: '/io.snap' },
      	{ recid: 2, location: 'Line 237', resource: '/math/Math.snap' },
      	{ recid: 3, location: 'Line 44', resource: '/loops.snap' }
  	];
    
    $().w2grid({ 
        name: 'problems',
        columns: [
            { field: 'location', caption: 'Location', size: '13%', sortable: true, resizable: true },
            { field: 'resource', caption: 'Resource', size: '43%', sortable: true, resizable: true },
            { field: 'description', caption: 'Description', size: '43%', sortable: true, resizable: true },
        ],
        onClick: function(event) {
            var grid = this;
            var form = w2ui.form;
            console.log(event);
            event.onComplete = function () {
                var sel = grid.getSelection();
                console.log(sel);
                if (sel.length == 1) {
                    form.recid  = sel[0];
                    form.record = $.extend(true, {}, grid.get(sel[0]));
                    form.refresh();
                } else {
                    form.clear();
                }
            }
        }
    });
    w2ui['problems'].records = problemRecords;
    
    $().w2grid({ 
        name: 'variables',
        columns: [
            { field: 'fname', caption: 'First Name', size: '33%', sortable: true, searchable: true },
            { field: 'lname', caption: 'Last Name', size: '33%', sortable: true, searchable: true },
            { field: 'email', caption: 'Email', size: '33%' },
            { field: 'sdate', caption: 'Start Date', size: '120px', render: 'date' },
        ],
        onClick: function(event) {
            var grid = this;
            var form = w2ui.form;
            console.log(event);
            event.onComplete = function () {
                var sel = grid.getSelection();
                console.log(sel);
                if (sel.length == 1) {
                    form.recid  = sel[0];
                    form.record = $.extend(true, {}, grid.get(sel[0]));
                    form.refresh();
                } else {
                    form.clear();
                }
            }
        }
    });
    
    $().w2grid({ 
        name: 'breakpoints',
        columns: [
	          { field: 'location', caption: 'Location', size: '13%', sortable: true, resizable: true },
	          { field: 'resource', caption: 'Resource', size: '86%', sortable: true, resizable: true },
        ],
        onClick: function(event) {
            var grid = this;
            var form = w2ui.form;
            console.log(event);
            event.onComplete = function () {
                var sel = grid.getSelection();
                console.log(sel);
                if (sel.length == 1) {
                    form.recid  = sel[0];
                    form.record = $.extend(true, {}, grid.get(sel[0]));
                    form.refresh();
                } else {
                    form.clear();
                }
            }
        }
    });
    w2ui['breakpoints'].records = breakpointRecords;
    
    w2ui['mainLayout'].content('top', w2ui['topLayout']);
    w2ui['mainLayout'].content('main', w2ui['blueLayout']);
    w2ui['blueLayout'].content('bottom', w2ui['tabLayout']);
    
    w2ui['tabLayout'].content('main', "<div style='overflow: scroll; font-family: monospace;' id='console'></div>");
    w2ui['tabLayout'].refresh();
    
}

registerModule("project", "Project module: project.js", createLayout, ["common", "socket", "console", "problem", "editor", "spinner", "tree"]);
