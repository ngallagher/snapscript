var treeVisible = false;

function reloadTree(socket, type, text) {
   createTree("/" + document.title, "explorer", "explorerTree", "/.", false, handleTreeMenu, function(event, data) {
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

registerModule("explorer", "Explorer module: explorer.js", showTree, [ "common", "spinner", "tree", "commands" ]);