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

function openTreeDialog(expandPath, foldersOnly, saveCallback) {
   var project = document.title;
   var folder = "/";
   var dialogTitle = "Save As";
   if(expandPath != null) {
      dialogTitle = "Save Changes";
   }
   w2popup.open({
      title : dialogTitle,
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
         saveCallback(path);
         setTimeout(function () { 
            w2popup.unlock();
            w2popup.close();
         }, 2000);
   });
   $("#dialogCancel").click(function(){
         w2popup.close();
   });
   var file = extractTreeFile(expandPath);
   if(file != null) {
      $('#dialogFile').html(file);
   }
   expandPath = extractTreePath(expandPath);
   createTree("dialog", "dialogTree", expandPath, foldersOnly, function(event, data) {
      var path = extractTreePath(data.node.tooltip);
      if (data.node.isFolder()) {
         folder = path;
         $('#dialogFile').html("");
      } else {
         var file = extractTreeFile(data.node.tooltip);
         $('#dialogFile').html(file);
      }
   });
}

//registerModule("dialog", "Dialog module: dialog.js", startDialog, [ "common", "socket" ]);