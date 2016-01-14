var subscription = {};
var routes = {};
var socket = null;
var connections = 0;
var attempts = 0;
var total = 1;

function startSocket() {
   createSubscription();
   setTimeout(openSocket, 1000); /* delay connect */
}

function refreshSocket() {
   if(socket != null) {
      socket.close();
   }
}

function createSubscription() {
   var host = window.document.location.hostname;
   var port = window.document.location.port;
   var scheme = window.document.location.protocol;
   var path = window.document.location.pathname;
   var query = window.document.location.search;

   subscription['panic'] = false;
   subscription['query'] = query;

   var address = "ws://";

   if (scheme.indexOf("https") == 0) {
      address = "wss://"
   }
   address += host;
   
   if((port - parseFloat(port) + 1) >= 0) {
      address += ":";
      address += port;
   }   
   var segments = path.split("/");

   if (segments.length > 2) {
      address += "/connect/" + segments[2];
   } else {
      address += "/connect"
   }   
   address += query;

   subscription['address'] = address;
}

function disableRoutes() {
   if (subscription.panic == false) {
      subscription['panic'] = true;
      socket.close();      
   }
}

function enableRoutes() {
   if (subscription.panic == true) {
      subscription['panic'] = false;   
      socket.close();
   }
}

function openSocket() {
   socket = new WebSocket(subscription.address);

   socket.onopen = function() {
      attempts = 1;
      connections++;
      console.log("Socket connected to '" + subscription.address + "'");
   };

   socket.onerror = function(message) {
      console.log("Error connecting to '" + subscription.address + "'");
   };

   socket.onclose = function(message) {
      var exponent = Math.pow(2, attempts++);
      var interval = (exponent - 1) * 1000;
      var reference = openSocket();

      if (interval > 30 * 1000) {
         interval = 30 * 1000;
      }
      setTimeout(reference, interval);
      console.log("Connection closed to '" + subscription.address + "' reconnecting in " + interval + " ms");
   };

   socket.onmessage = function(message) {
      var data = message.data;
      var index = data.indexOf(':');
      var value = null;
      var type = null;
      
      if(index != -1) {
         value = data.substring(index + 1);
         type = data.substring(0, index);
      } else {
         type = data;
      }
      var route = routes[type];
      
      if(route != undefined) {
         if(!subscription.panic) { // this might cause problems with missing messages
            route(this, type, value);
         }
      } else {
         console.log("No route defined for '" + code+ "'");
      }
   };
}

function createRoute(code, method) {
   var name = code.toUpperCase();
   
   if(name != code) {
      alert("Illegal route '" + name + "' is not in upper case");
   }
   var route = routes[code];
   
   if(route == null) {
      routes[code] = method; // perhaps we should disconnect on every new route?
      refreshSocket();
   }
}

registerModule("socket", "Socket subscription module: socket.js", startSocket, []);