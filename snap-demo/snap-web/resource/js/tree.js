var treeVisible = false;

function showTree() {
   if(treeVisible == false) {     
      var func = function(){
    	  createTree();
      }
      window.setTimeout(func, 1000);
   }

}

function openTreeFile(path) {
    $.get(path, function(response) {
    	updateEditor(response);
    });
}

function createTree() {
	$(document).ready(function(){
		var project = document.title;
	    $.get('/tree/' + project, function(response) {
	        $('#explorer').html(response);
	    })
	    function showFancyTree() {
	    	// using default options
	    	$("#tree").fancytree({
	    		  click: function(event, data) {
	    			 if(!data.node.isFolder()){
	    			    openTreeFile(data.node.tooltip);
	    			 }
				  }
	    	});
        }
        window.setTimeout(showFancyTree, 1000);
      });
}

registerModule("tree", "Tree module: tree.js", showTree, ["spinner"]);