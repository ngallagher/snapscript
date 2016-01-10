var treeVisible = false;

function showTree() {
   if (treeVisible == false) {
      var func = function() {
         createTree("explorer", "explorerTree", function(event, data) {
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

function createTree(element, id, callback) { // #explorer
   $(document).ready(function() {
      var project = document.title;
      $.get('/tree/' + project + "?id=" + id, function(response) {
         $('#' + element).html(response);
      })
      function showFancyTree() {
         // using default options
         $('#' + id).fancytree({
            click : callback
         });
      }
      window.setTimeout(showFancyTree, 500);
   });
}

registerModule("tree", "Tree module: tree.js", showTree, [ "spinner" ]);