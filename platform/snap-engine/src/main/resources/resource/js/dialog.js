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

function openTreeDialog(resourceDetails, foldersOnly, saveCallback) {
   if (resourceDetails != null) {
      createTreeDialog(resourceDetails, foldersOnly, saveCallback, "Save Changes");
   } else {
      createTreeDialog(resourceDetails, foldersOnly, saveCallback, "Save As");
   }
}

function newFileTreeDialog(resourceDetails, foldersOnly, saveCallback){
   createTreeDialog(resourceDetails, foldersOnly, saveCallback, "New File");
}

function newDirectoryTreeDialog(resourceDetails, foldersOnly, saveCallback){
   createTreeDialog(resourceDetails, foldersOnly, saveCallback, "New Directory");
}

function createTreeDialog(resourceDetails, foldersOnly, saveCallback, dialogTitle) {
   var project = document.title;
   var dialogExpandPath = "/";

   if (resourceDetails != null) {
      dialogExpandPath = resourceDetails.projectDirectory; // /src/blah
   }
   w2popup.open({
      title : dialogTitle,
      body : '<div id="dialogContainer"><div id="dialog"></div></div><div id="dialogFolder">'+dialogExpandPath+'</div><div id="dialogFile" onClick="this.contentEditable=\'true\';"></div>',
      buttons : '<button id="dialogSave" class="btn">Save</button><button id="dialogCancel" class="btn">Cancel</button>',
      width : 500,
      height : 400,
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
   $("#dialogSave").click(function() {
//      w2popup.lock('Saving', true);
      var dialogFileName = $('#dialogFile').html();
      var dialogFolder = $('#dialogFolder').html();
      var dialogProjectPath = dialogFolder + "/" + dialogFileName; // /src/blah/script.snap
      var dialogPathDetails = createResourcePath(dialogProjectPath); 
      
      saveCallback(dialogPathDetails);
      w2popup.close();
//      setTimeout(function() {
//         w2popup.unlock();
//         w2popup.close();
//      }, 1000);
   });
   $("#dialogCancel").click(function() {
      w2popup.close();
   });
   if (resourceDetails != null) {
      $('#dialogFolder').html(resourceDetails.projectDirectory); // /src/blah
      $('#dialogFile').html(resourceDetails.fileName); // script.snap
   }
   createTree("dialog", "dialogTree", dialogExpandPath, foldersOnly, function(event, data) {
      var selectedFileDetails = createResourcePath(data.node.tooltip);

      if (data.node.isFolder()) {
         $('#dialogFolder').html(selectedFileDetails.projectDirectory);
         $('#dialogFile').html("");
      } else {
         $('#dialogFolder').html(selectedFileDetails.projectDirectory); // /src/blah
         $('#dialogFile').html(selectedFileDetails.fileName); // file.snap
      }
   });
}

