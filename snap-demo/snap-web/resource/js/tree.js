var treeVisible = false;

function showTree() {
   if (treeVisible == false) {
      var func = function() {
         createTree("explorer", "explorerTree", null, false, function(event, data) {
            if (!data.node.isFolder()) {
               openTreeFile(data.node.tooltip);
            }
         });
      }
      window.setTimeout(func, 500);
   }

}

function openTreeFile(path) {
   $.get(path, function(response) {
      updateEditor(response, path);
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