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

function openTreeFile(path, afterLoad) {
   $.get(path, function(response) {
      updateEditor(response, path);
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

function extractTreePath(path) {
   if(path != null) {
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
   }
   return path;
}

function extractTreePathNoFile(path) {
   if(path != null) {
      if(path.startsWith("/resource")) {
         var segments = path.split("/");
         if(segments.length > 3) {
            path = "";
            for(var i = 3; i < segments.length - 1; i++) {
               path += "/" + segments[i];
            }
         } else {
            path = "/";
         }
      } else {
         var dotIndex = path.lastIndexOf(".");
         var slashIndex = path.lastIndexOf("/");
         
         if(dotIndex == -1) {
            return path;
         }
         if(slashIndex == -1) {
            return "/src";
         }
         if(dotIndex < slashIndex) {
            return path;
         }
         return path.substring(0, slashIndex);
      }
   }
   return path;
}

function extractTreeFile(path) {
   if(path != null) {
      var segments = path.split("/");
      var length = segments.length;
      return segments[length-1];
   }
   return path;
}

function buildTreeFile(path) {
   if(path != null) {
      if(!path.startsWith("/resource")) {
         if(path.startsWith("/")) {
            return "/resource/" + document.title + path;
         } else {
            return "/resource/" + document.title + "/" + path;
         }
      }
   }
   return path;
}

registerModule("tree", "Tree module: tree.js", showTree, [ "spinner" ]);