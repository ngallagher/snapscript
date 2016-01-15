
function showThreads() {
   $(document).ready(function() {
      function showFancyTree() {
         $("#threadTree").fancytree({
            source: {url: "/threads.json"}
         });
      }
      window.setTimeout(showFancyTree, 500);
   });
}