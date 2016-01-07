var treeVisible = false;

function showTree() {
   if(treeVisible == false) {     
      var func = function(){
    	  createTree();
      }
      window.setTimeout(func, 1000);
   }

}

function createTree() {
	$(document).ready(function(){
	    $.get('/tree', function(response) {
	        $('#explorer').html(response);
	    })
	    function showFancyTree() {
	    	// using default options
	    	$("#tree").fancytree();
        }
        window.setTimeout(showFancyTree, 1000);
      });
}

registerModule("tree", "Tree module: tree.js", showTree, ["spinner"]);