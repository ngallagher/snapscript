
function trackHistory() {
   $(window).on('hashchange', function() {
      updateEditorFromHistory();
   });
   setTimeout(updateEditorFromHistory, 200);
}

function updateEditorFromHistory(){
   var location = window.location.hash;
   var hashIndex = location.indexOf('#');
   
   if(hashIndex != -1) {
      var resource = location.substring(hashIndex + 1);
      var resourceData = createResourcePath(resource);
      
      openTreeFile(resourceData.resourcePath, function() {
         editor.setReadOnly(true); // make sure its editable
      });
   }
}

registerModule("history", "History module: history.js", trackHistory, [ "common", "editor" ]);