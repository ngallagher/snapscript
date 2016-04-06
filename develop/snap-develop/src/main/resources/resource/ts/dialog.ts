function openAlertDialog(message) {
   w2popup.open({
      title : 'Alert',
      body : '<div class="dialog">'+
             '    <div style="style="text-align: center">'+
             '       <div style="display: inline-block;">'+message+'</div>'+
             '    </div>'+
             '</div>',
      buttons : '<button class="btn" onclick="w2popup.close();">Close</button>',
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
      createProjectDialog(resourceDetails, foldersOnly, saveCallback, "Save Changes");
   } else {
      createProjectDialog(resourceDetails, foldersOnly, saveCallback, "Save As");
   }
}

function newFileTreeDialog(resourceDetails, foldersOnly, saveCallback){
   createProjectDialog(resourceDetails, foldersOnly, saveCallback, "New File");
}

function newDirectoryTreeDialog(resourceDetails, foldersOnly, saveCallback){
   createProjectDialog(resourceDetails, foldersOnly, saveCallback, "New Directory");
}

function createProjectDialog(resourceDetails, foldersOnly, saveCallback, dialogTitle) {
   createTreeDialog(resourceDetails, foldersOnly, saveCallback, dialogTitle, "/" +document.title)
}

function createTreeDialog(resourceDetails, foldersOnly, saveCallback, dialogTitle, treePath) {
   var dialogExpandPath = "/";

   if (resourceDetails != null) {
      dialogExpandPath = resourceDetails.projectDirectory; // /src/blah
   }
   w2popup.open({
      title : dialogTitle,
      body : '<div id="dialogContainer">'+
             '   <div id="dialog"></div>'+
             '</div>'+
             '<div id="dialogFolder">'+dialogExpandPath+'</div>'+
             '<div id="dialogFile" onkeydown="return submitDialog(event);" onclick="this.contentEditable=\'true\';"></div>',
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
      var dialogFileName = $('#dialogFile').html();
      var dialogFolder = $('#dialogFolder').html();
      var dialogProjectPath = dialogFolder + "/" + dialogFileName; // /src/blah/script.snap
      var dialogPathDetails = createResourcePath(dialogProjectPath); 
      
      saveCallback(dialogPathDetails);
      w2popup.close();
   });
   $("#dialogCancel").click(function() {
      w2popup.close();
   });
   if (resourceDetails != null) {
      $('#dialogFolder').html(cleanResourcePath(resourceDetails.projectDirectory)); // /src/blah
      $('#dialogFile').html(resourceDetails.fileName); // script.snap
   }
   createTree(treePath, "dialog", "dialogTree", dialogExpandPath, foldersOnly, null, function(event, data) {
      var selectedFileDetails = createResourcePath(data.node.tooltip);

      if (data.node.isFolder()) {
         $('#dialogFolder').html(cleanResourcePath(selectedFileDetails.projectDirectory));
         $('#dialogFile').html("");
      } else {
         $('#dialogFolder').html(cleanResourcePath(selectedFileDetails.projectDirectory)); // /src/blah
         $('#dialogFile').html(selectedFileDetails.fileName); // file.snap
      }
   });
}

function createTreeOpenDialog(openCallback, closeCallback, dialogTitle, treePath) {
   var completeFunction = function() {
      var dialogFolder = $('#dialogPath').html();
      var dialogPathDetails = createResourcePath(dialogFolder); 
      var projectName = dialogPathDetails.projectDirectory;
      
      if(projectName.startsWith("/")) {
         projectName = projectName.substring(1);
      }
      openCallback(dialogPathDetails, projectName);
   };
   w2popup.open({
      title : dialogTitle,
      body : '<div id="dialogContainerBig">'+
             '   <div id="dialog"></div>'+
             '</div>'+
             '<div id="dialogPath" onkeydown="return submitDialog(event);" onclick="this.contentEditable=\'true\';"></div>',
      buttons : '<button id="dialogSave" class="btn">Open</button>',
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
         closeCallback(); // this should probably be a parameter
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
      completeFunction();
      w2popup.close();
   });
   createTreeOfDepth(treePath, "dialog", "dialogTree", "/" + document.title, true, null, function(event, data) {
      var selectedFileDetails = createResourcePath(data.node.tooltip);
      var projectName = selectedFileDetails.projectDirectory;
      
      if(projectName.startsWith("/")) {
         projectName = projectName.substring(1);
      }
      $('#dialogPath').html(projectName);
   }, 2);
}

function submitDialog(e) {
   var evt = e || window.event
   // "e" is the standard behavior (FF, Chrome, Safari, Opera),
   // while "window.event" (or "event") is IE's behavior
   if ( evt.keyCode === 13 ) {
      $("#dialogSave").click(); // force the click
       // Do something
       // You can disable the form submission this way:
       return false
   }
}

registerModule("dialog", "Dialog module: dialog.js", null, [ "common", "tree" ]);
