var FILE_TYPES = [".snap", ".html", ".xml", ".json", ".properties", ".txt", ".png", ".gif", ".js", ".sql"];
var treeVisible = false;

function reloadTree(socket, type, text) {
   createTree("explorer", "explorerTree", "/.", false, function(event, data) {
      if (!data.node.isFolder()) {
         openTreeFile(data.node.tooltip, function(){});
      }
   });
}

function showTree() {
   if (treeVisible == false) {
      window.setTimeout(reloadTree, 500);
      treeVisible = true;
   }
   createRoute("RELOAD_TREE", reloadTree);

}

function openTreeFile(resourcePath, afterLoad) {
   $.get(resourcePath, function(response) {
      var mode = resolveEditorMode(resourcePath);
      
      if(mode == null) {
         var resourceBlob = new Blob([response], {type: "application/octet-stream"});
         var resourceFile = resourcePath.replace(/.*\//, "");
         
         saveAs(resourceBlob, resourceFile);
      } else {
         updateEditor(response, resourcePath);
      }
      afterLoad();
   });
}

function createTree(element, id, expandPath, foldersOnly, clickCallback) { // #explorer
   $(document).ready(function() {
      var project = document.title;
      var requestPath = '/tree/' + project + "?id=" + id + "&folders=" + foldersOnly;
      
      if(expandPath != null) {
         requestPath += "&expand="+expandPath;
      }
      $.get(requestPath, function(response) {
         $('#' + element).html(response);
      })
      function showFancyTree() {
         // using default options
         $('#' + id).fancytree({
            click : clickCallback
         });
         $("#" + id).contextmenu({
            delegate: "span.fancytree-title",
//            menu: "#options",
            menu: [
                {title: "&nbsp;New", uiIcon: "menu-new", children: [
                   {title: "&nbsp;File", cmd: "newFile", uiIcon: "menu-new"},
                   {title: "&nbsp;Directory", cmd: "newDirectory", uiIcon: "menu-new"}
                   ]},              
                {title: "&nbsp;Save", cmd: "saveFile", uiIcon: "menu-save"},             
                {title: "&nbsp;Delete", cmd: "deleteFile", uiIcon: "menu-trash", disabled: false },
                {title: "&nbsp;Run", cmd: "runScript", uiIcon: "menu-run"} //,              
                //{title: "----"},
                //{title: "Edit", cmd: "edit", uiIcon: "ui-icon-pencil", disabled: true },
                //{title: "Delete", cmd: "delete", uiIcon: "ui-icon-trash", disabled: true }
                ],
            beforeOpen: function(event, ui) {
              var node = $.ui.fancytree.getNode(ui.target);
//                      node.setFocus();
              node.setActive();
              var $menu = ui.menu,
              $target = ui.target,
              extraData = ui.extraData; // passed when menu was opened by call to open()

           // console.log("beforeOpen", event, ui, event.originalEvent.type);

              ui.menu.zIndex( $(event.target).zIndex() + 2000);
            },
            select: function(event, ui) {
              var node = $.ui.fancytree.getNode(ui.target);
              var resourcePath = createResourcePath(node.tooltip);
              var commandName = ui.cmd;
              var elementId = ui.key;
              
              handleTreeMenu(resourcePath, commandName, elementId);
            }
          });         
      }
      window.setTimeout(showFancyTree, 500);
   });
}

function handleTreeMenu(resourcePath, commandName, elementId) {
   if(commandName == "runScript") {
      openTreeFile(resourcePath.resourcePath, function(){
         runScript();
      });
   }else if(commandName == "newFile") {
      newFile();
   }else if(commandName == "newDirectory") {
      newDirectory();
   }else if(commandName == "saveFile") {
      openTreeFile(resourcePath.resourcePath, function(){
         saveFile();
      });
   }else if(commandName == "deleteFile") {
      if(isResourceFolder(resourcePath.resourcePath)) {
         deleteDirectory(resourcePath);
      } else {
         deleteFile(resourcePath);
      }
   }
}

function isResourceFolder(path) {
   if(!path.endsWith("/")) {
      var parts = path.split(".");
      
      if(path.length === 1 || (parts[0] === "" && parts.length === 2)) {
          return true;
      }
      var extension = parts.pop();
      var slash = extension.indexOf('/');
      
      return slash >= 0;
   }
   return true;
}

function cleanResourcePath(path) {
   if(path != null) {
      return path.replace(/\/*/, "/"); // replace // with /
   }
   return null;
}

function createResourcePath(path) { 
   var resourcePathPrefix = "/resource/" + document.title + "/";
   var resourcePathRoot = "/resource/" + document.title;
   
   if(path == resourcePathRoot || path == resourcePathPrefix) { // its the root /
      var currentPathDetails = {
         resourcePath: resourcePathPrefix, // /resource/<project>/blah/script.snap
         projectPath: "/", // /blah/script.snap
         projectDirectory: "/", // /blah
         filePath: "/", // /blah/script.snap
         fileName: null, // script.snap
         fileDirectory: "/" // /blah
      };
      var currentPathText = JSON.stringify(currentPathDetails);
      //console.log("createResourcePath(" + path + "): " + currentPathText);
      return currentPathDetails;
   }
   console.log("createResourcePath(" + path + ")");
   
   if(!path.startsWith("/")) {  // script.snap
      path = "/" + path; // /snap.script
   }
   if(!path.startsWith(resourcePathPrefix)) { // /resource/<project>/(<file-path>)
      path = "/resource/" + document.title + path;
   }
   var isFolder = isResourceFolder(path); // /resource/<project>/blah/
   var pathSegments = path.split("/"); // [0="", 1="resource", 2="<project>", 3="blah", 4="script.snap"]
   var currentResourcePath = "/resource/" + document.title;
   var currentProjectPath = "";
   var currentProjectDirectory = "";   
   var currentFileName = null;
   var currentFilePath = "";
   var currentFileDirectory = "";
   
   for(var i = 3; i < pathSegments.length; i++) { 
      currentResourcePath += "/" + pathSegments[i];
      currentProjectPath += "/" + pathSegments[i];
      currentFilePath += "/" + pathSegments[i];
   }
   if(isFolder) { // /resource/<project>/blah/
      if(pathSegments.length > 3) {
         for(var i = 3; i < pathSegments.length; i++) { 
            currentProjectDirectory += "/" + pathSegments[i];
            currentFileDirectory += "/" + pathSegments[i];
         }
      } else {
         currentFileDirectory = "/";
      }
   } else { // /resource/<project>/blah/script.snap
      var currentFileName = pathSegments[pathSegments.length - 1];
      
      if(pathSegments.length > 4) {
         for(var i = 3; i < pathSegments.length - 1; i++) { 
            currentProjectDirectory += "/" + pathSegments[i];
            currentFileDirectory += "/" + pathSegments[i];
         }
      } else {
         currentFileDirectory = "/";
      }
   }
   var currentPathDetails = {
      resourcePath: cleanResourcePath(currentResourcePath), // /resource/<project>/blah/script.snap
      projectPath: cleanResourcePath(currentProjectPath), // /blah/script.snap
      projectDirectory: cleanResourcePath(currentProjectDirectory == "" ? "/" : currentProjectDirectory), // /blah
      filePath: cleanResourcePath(currentFilePath), // /blah/script.snap
      fileName: cleanResourcePath(currentFileName), // script.snap
      fileDirectory: cleanResourcePath(currentFileDirectory) // /blah
   };
   var currentPathText = JSON.stringify(currentPathDetails);
   //console.log("createResourcePath(" + path + "): " + currentPathText);
   return currentPathDetails;
}

registerModule("tree", "Tree module: tree.js", showTree, [ "spinner" ]);