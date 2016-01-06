var spinnerHiding = false;

function runScript(){
   //reconnect("best");
   document.getElementById("mainMarket").className = ""; 
   document.getElementById("mainMarket").className = "btn selected";
   document.getElementById("mainMyPrices").className = "";  
   document.getElementById("mainMyPrices").className = "btn";
   document.getElementById("litMyPrices").className = "";   
   document.getElementById("litMyPrices").className = "btn";
   document.getElementById("switchMyPrices").className = "";   
   document.getElementById("switchMyPrices").className = "btn";
   //location.href = "http://localhost:6060/grid.html?user=tom&company=ANZ&type=best";
}

function saveScript(){
   //reconnect("company");
   document.getElementById("mainMarket").className = ""; 
   document.getElementById("mainMarket").className = "btn"; 
   document.getElementById("mainMyPrices").className = "";  
   document.getElementById("mainMyPrices").className = "btn selected";
   document.getElementById("litMyPrices").className = "";   
   document.getElementById("litMyPrices").className = "btn selected";
   document.getElementById("switchMyPrices").className = "";   
   document.getElementById("switchMyPrices").className = "btn selected";                  
   //location.href = "http://localhost:6060/grid.html?user=tom&company=ANZ&type=company";
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
            { type: 'left', size: '20%', resizable: true, style: pstyle, content: "<div id='explorer'>" +
            	"	<ol class='tree'>"+
            	"		<li>"+
            	"			<label for='folder1'>Folder 1</label> <input type='checkbox' checked disabled id='folder1' />"+
            	"			<ol>"+
            	"				<li class='file'><a href='document.html.pdf'>File 1</a></li>"+
            	"				<li>"+
            	"					<label for='subfolder1'>Subfolder 1</label> <input type='checkbox' id='subfolder1' />"+
            	"					<ol>"+
            	"						<li class='file'><a href=''>Filey 1</a></li>"+
            	"						<li>"+
            	"							<label for='subsubfolder1'>Subfolder 1</label> <input type='checkbox' id='subsubfolder1' />"+
            	"							<ol>"+
            	"								<li class='file'><a href=''>File 1</a></li>"+
            	"								<li>"+
            	"									<label for='subsubfolder2'>Subfolder 1</label> <input type='checkbox' id='subsubfolder2' />"+
            	"									<ol>"+
            	"										<li class='file'><a href=''>Subfile 1</a></li>"+
            	"										<li class='file'><a href=''>Subfile 2</a></li>"+
            	"										<li class='file'><a href=''>Subfile 3</a></li>"+
            	"										<li class='file'><a href=''>Subfile 4</a></li>"+
            	"										<li class='file'><a href=''>Subfile 5</a></li>"+
            	"										<li class='file'><a href=''>Subfile 6</a></li>"+
            	"									</ol>"+
            	"								</li>"+
            	"							</ol>"+
            	"						</li>"+
            	"						<li class='file'><a href=''>File 3</a></li>"+
            	"						<li class='file'><a href=''>File 4</a></li>"+
            	"						<li class='file'><a href=''>File 5</a></li>"+
            	"						<li class='file'><a href=''>File 6</a></li>"+
            	"					</ol>"+
            	"				</li>"+
            	"			</ol>"+
            	"		</li>"+
            	"		<li>"+
            	"			<label for='folder2'>Folder 2</label> <input type='checkbox' id='folder2' />"+
            	"			<ol>"+
            	"				<li class='file'><a href=''>File 1</a></li>"+
            	"				<li>"+
            	"					<label for='subfolder2'>Subfolder 1</label> <input type='checkbox' id='subfolder2' />"+
            	"					<ol>"+
            	"						<li class='file'><a href=''>Subfile 1</a></li>"+
            	"						<li class='file'><a href=''>Subfile 2</a></li>"+
            	"						<li class='file'><a href=''>Subfile 3</a></li>"+
            	"						<li class='file'><a href=''>Subfile 4</a></li>"+
            	"						<li class='file'><a href=''>Subfile 5</a></li>"+
            	"						<li class='file'><a href=''>Subfile 6</a></li>"+
            	"					</ol>"+
            	"				</li>"+
            	"			</ol>"+
            	"		</li>"+
            	"		<li>"+
            	"			<label for='folder3'>Folder 3</label> <input type='checkbox' id='folder3' />"+
            	"			<ol>"+
            	"				<li class='file'><a href=''>File 1</a></li>"+
            	"				<li>"+
            	"					<label for='subfolder3'>Subfolder 1</label> <input type='checkbox' id='subfolder3' />"+
            	"					<ol>"+
            	"						<li class='file'><a href=''>Subfile 1</a></li>"+
            	"						<li class='file'><a href=''>Subfile 2</a></li>"+
            	"						<li class='file'><a href=''>Subfile 3</a></li>"+
            	"						<li class='file'><a href=''>Subfile 4</a></li>"+
            	"						<li class='file'><a href=''>Subfile 5</a></li>"+
            	"						<li class='file'><a href=''>Subfile 6</a></li>"+
            	"					</ol>"+
            	"				</li>"+
            	"			</ol>"+
            	"		</li>"+
            	"		<li>"+
            	"			<label for='folder4'>Folder 4</label> <input type='checkbox' id='folder4' />"+
            	"			<ol>"+
            	"				<li class='file'><a href=''>File 1</a></li>"+
            	"				<li>"+
            	"					<label for='subfolder4'>Subfolder 1</label> <input type='checkbox' id='subfolder4' />"+
            	"					<ol>"+
            	"						<li class='file'><a href=''>Subfile 1</a></li>"+
            	"						<li class='file'><a href=''>Subfile 2</a></li>"+
            	"						<li class='file'><a href=''>Subfile 3</a></li>"+
            	"						<li class='file'><a href=''>Subfile 4</a></li>"+
            	"						<li class='file'><a href=''>Subfile 5</a></li>"+
            	"						<li class='file'><a href=''>Subfile 6</a></li>"+
            	"					</ol>"+
            	"				</li>"+
            	"			</ol>"+
            	"		</li>"+
            	"		<li>"+
            	"			<label for='folder5'>Folder 5</label> <input type='checkbox' id='folder5' />"+
            	"			<ol>"+
            	"				<li class='file'><a href=''>File 1</a></li>"+
            	"				<li>"+
            	"					<label for='subfolder5'>Subfolder 1</label> <input type='checkbox' id='subfolder5' />"+
            	"					<ol>"+
            	"						<li class='file'><a href=''>Subfile 1</a></li>"+
            	"						<li class='file'><a href=''>Subfile 2</a></li>"+
            	"						<li class='file'><a href=''>Subfile 3</a></li>"+
            	"						<li class='file'><a href=''>Subfile 4</a></li>"+
            	"						<li class='file'><a href=''>Subfile 5</a></li>"+
            	"						<li class='file'><a href=''>Subfile 6</a></li>"+
            	"					</ol>"+
            	"				</li>"+
            	"			</ol>"+
            	"		</li>"+
            	"	</ol>"+

          "</div>"
            },        
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
					"<button id='runScript' class='btn selected' onclick='runScript()'>Run</button>" +
					"</td>" +
					"<td>" +
					"<button id='stopScript' class='btn panic' onclick='stopScript()'>Stop</button>" +
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
                        { id: 'tab2', caption: 'Problems' }
                    ],
                    onClick: function (event) {
                        if(event.target == 'tab1') {
                           w2ui['tabLayout'].content('main', "<div id='console'></div>");                           
                        } else {
                           w2ui['tabLayout'].content('main', "<div id='problems'></div>");                           
                        }
                        w2ui['tabLayout'].refresh();
                    }
                }
            }
        ]
    });    
    
    w2ui['mainLayout'].content('top', w2ui['topLayout']);
    w2ui['mainLayout'].content('main', w2ui['blueLayout']);
    w2ui['blueLayout'].content('bottom', w2ui['tabLayout']);
    
    w2ui['tabLayout'].content('main', "<div id='console'></div>");
    w2ui['tabLayout'].refresh();
    
}

registerModule("ide", "Bonds module: bonds.js", createLayout, ["common", "socket", "grid", "spinner"]);
