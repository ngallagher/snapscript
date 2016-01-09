function populateMenu() {
   var host = window.document.location.hostname;
   var port = window.document.location.port;
   var scheme = window.document.location.protocol;
   var path = window.document.location.pathname;
   var user = extractParameter("user");
   var company = extractParameter("company");
   var address = "http://";

   if (scheme.indexOf("https") == 0) {
      address = "https://"
   }
   address += host;
   
   if((port - parseFloat(port) + 1) >= 0) {
      address += ":";
      address += port;
   }   
   var segments = path.split("/");

   if (segments.length > 2) {
      address += "/" + segments[1] + "/screen"
   } else {
      address += "/screen"
   }
   document.getElementById("indicativeItem").href = address + "/indicative/index.html?user=" + user + "&company=" + company + "&tables=indicativeGrid";
   document.getElementById("contributionItem").href = address + "/contribution/index.html?user=" + user + "&company=" + company + "&tables=contributionGrid";
   document.getElementById("bestItem").href = address + "/best/index.html?user=" + user + "&company=" + company + "&tables=bestGrid";
   document.getElementById("telemetryItem").href = address + "/telemetry/index.html?user=" + user + "&company=" + company + "&tables=telemetryGrid";   
}

registerModule("menu", "Menu module: menu.js", populateMenu, []);