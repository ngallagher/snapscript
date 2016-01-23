var FILE_TYPES = [".snap", ".html", ".xml", ".json", ".properties", ".txt", ".png", ".gif", ".js", ".sql"];
var treeVisible = false;

function reloadTree(socket, type, text) {
   createTree("explorer", "explorerTree", null, false, function(event, data) {
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
      updateEditor(response, resourcePath);
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
      }
      window.setTimeout(showFancyTree, 500);
   });
}

function createResourcePath(path) { 
   var resourcePathPrefix = "/resource/" + document.title + "/src/";
   
   console.log("createResourcePath(" + path + ")");
   
   if(!path.startsWith("/")) {  // script.snap
      path = "/" + path; // /snap.script
   }
   if(!path.startsWith(resourcePathPrefix)) { // /resource/<project>/src(<file-path>)
      if(!path.startsWith("/src")) {
         path = "/resource/" + document.title + "/src" + path;
      } else {
         path = "/resource/" + document.title + path;
      } 
   }
   var isFolder = path.indexOf(".snap", path.length - ".snap".length) == -1; // /resource/<project>/src/blah/
   var pathSegments = path.split("/"); // [0="", 1="resource", 2="<project>", 3="src", 4="blah", 5="script.snap"]
   var currentResourcePath = "/resource/" + document.title + "/src";
   var currentProjectPath = "/src";
   var currentProjectDirectory = "/src";   
   var currentFileName = null;
   var currentFilePath = "";
   var currentFileDirectory = "";
   
   for(var i = 4; i < pathSegments.length; i++) { 
      currentResourcePath += "/" + pathSegments[i];
      currentProjectPath += "/" + pathSegments[i];
      currentFilePath += "/" + pathSegments[i];
   }
   if(isFolder) { // /resource/<project>/src/blah/
      if(pathSegments.length > 4) {
         for(var i = 4; i < pathSegments.length; i++) { 
            currentProjectDirectory += "/" + pathSegments[i];
            currentFileDirectory += "/" + pathSegments[i];
         }
      } else {
         currentFileDirectory = "/";
      }
   } else { // /resource/<project>/src/blah/script.snap
      var currentFileName = pathSegments[pathSegments.length - 1];
      
      if(pathSegments.length > 5) {
         for(var i = 4; i < pathSegments.length - 1; i++) { 
            currentProjectDirectory += "/" + pathSegments[i];
            currentFileDirectory += "/" + pathSegments[i];
         }
      } else {
         currentFileDirectory = "/";
      }
   }
   var currentPathDetails = {
      resourcePath: currentResourcePath, // /resource/<project>/src/blah/script.snap
      projectPath: currentProjectPath, // /src/blah/script.snap
      projectDirectory: currentProjectDirectory, // /src/blah
      filePath: currentFilePath, // /blah/script.snap
      fileName: currentFileName, // script.snap
      fileDirectory: currentFileDirectory // /blah
   };
   var currentPathText = JSON.stringify(currentPathDetails);
   console.log("createResourcePath(" + path + "): " + currentPathText);
   return currentPathDetails;
}

registerModule("tree", "Tree module: tree.js", showTree, [ "spinner" ]);