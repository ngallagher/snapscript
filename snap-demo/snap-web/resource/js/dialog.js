function openConfirmDialog() {
   w2popup.open({
      title : 'Popup Title',
      body : '<div class="dialog">This is text inside the popup</div>',
      buttons : '<button class="btn" onclick="w2popup.close();">Close</button> ' + '<button class="btn" onclick="w2popup.lock(\'Loading\', true); '
            + '        setTimeout(function () { w2popup.unlock(); }, 2000);">Lock</button>',
      width : 500,
      height : 300,
      overflow : 'hidden',
      color : '#333',
      speed : '0.3',
      opacity : '0.8',
      modal : true,
      showClose : true,
      showMax : true,
      onOpen : function(event) {
         console.log('open');
      },
      onClose : function(event) {
         console.log('close');
      },
      onMax : function(event) {
         console.log('max');
      },
      onMin : function(event) {
         console.log('min');
      },
      onKeydown : function(event) {
         console.log('keydown');
      }
   });
}

function openTreeDialog(save) {
   var project = document.title;
   var folder = "/";
   w2popup.open({
      title : 'Save As',
      body : '<div id="dialogContainer"><div id="dialog"></div></div><div id="dialogFile" onClick="this.contentEditable=\'true\';"></div>',
      buttons : '<button id="dialogSave" class="btn">Save</button><button id="dialogCancel" class="btn">Cancel</button>',
      width : 500,
      height : 300,
      overflow : 'hidden',
      color : '#333',
      speed : '0.3',
      opacity : '0.8',
      modal : true,
      showClose : true,
      showMax : true,
      onOpen : function(event) {
         console.log('open');
      },
      onClose : function(event) {
         console.log('close');
      },
      onMax : function(event) {
         console.log('max');
      },
      onMin : function(event) {
         console.log('min');
      },
      onKeydown : function(event) {
         console.log('keydown');
      }
   });
   $("#dialogSave").click(function(){
         w2popup.lock('Saving', true);
         var resource = $('#dialogFile').html();
         var path = folder + "/" + resource;
         save(path);
         setTimeout(function () { 
            w2popup.unlock();
            w2popup.close();
         }, 2000);
   });
   $("#dialogCancel").click(function(){
         w2popup.close();
   });   
   createTree("dialog", "dialogTree", function(event, data) {
      var path = data.node.tooltip;
      if(path.startsWith("/resource")) {
         var segments = path.split("/");
         if(segments.length > 3) {
            path = "";
            for(var i = 3; i < segments.length; i++) {
               path += "/" + segments[i];
            }
         } else {
            path = "/";
         }
      }
      if (data.node.isFolder()) {
         folder = path;
         $('#dialogFile').html("");
      } else {
         var index = path.lastIndexOf("/");
         folder = path.substring(0, index);
         path = path.substring(index + 1);
         $('#dialogFile').html(path);
      }
   });
}
registerModule("dialog", "Dialog module: dialog.js", startDialog, [ "common", "socket" ]);