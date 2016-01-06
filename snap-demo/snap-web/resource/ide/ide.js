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
            //{ type: 'left', size: '20%', style: pstyle },        
            { type: 'main', size: '100%', style: pstyle }           
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
            { type: 'top', size: '60%', style: pstyle, content: "<div id='editor'>// TODO write code </div>" },        
            { type: 'bottom', size: '40%', style: pstyle,
                name: 'tabs',
                tabs: {
                    active: 'tab1',
                    tabs: [
                        { id: 'tab1', caption: 'Console' },
                        { id: 'tab2', caption: 'Problems' }
                    ],
                    onClick: function (event) {
                        if(event.target == 'tab1') {
                           w2ui['tabLayout'].content('main', w2ui['monitor']);                           
                        } else {
                           w2ui['tabLayout'].content('main', w2ui['blotter']);                           
                        }
                        w2ui['tabLayout'].refresh();
                    }
                }
            }            
        ]
    }); 
    
    $('').w2layout({
        name: 'tabLayout',
        padding: 0,
        panels: [
            { type: 'main', size: '100%', style: pstyle, resizable: false }                 
        ]
    });    
    
    w2ui['mainLayout'].content('top', w2ui['topLayout']);
    //w2ui['mainLayout'].content('left', w2ui['mainGrid']);
    w2ui['mainLayout'].content('main', w2ui['blueLayout']);
    
    //w2ui['blueLayout'].content('left', w2ui['litEFPGrid']);   
    //w2ui['blueLayout'].content('right', w2ui['litSwitchGrid']);
    
    w2ui['blueLayout'].content('bottom', w2ui['tabLayout']);
    w2ui['tabLayout'].content('main', w2ui['monitor']);  
    
}

registerModule("ide", "Bonds module: bonds.js", createLayout, ["common", "socket", "grid", "spinner"]);
