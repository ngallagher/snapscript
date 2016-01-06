var callbacks = {};
var calls = 0;


function startInvocations() {  
   setInterval(expireInvocations, 10000);
   createRoute('R', returnInvocation);
}

/*
 [socket] this argument is a connected websocket
 [service] this is the name of the service
 [method] this is the method to be called
 [arguments] an associative array of parameters
 [message] this is any string message to send
 [success] this is a function that is called on success 
 [failure] this is a function that is called on failure
 [wait] this is the length of time to wait for a response
   
 invoke(socket, 
       {
          service: "pricebook",
          method: "activate",
          arguments: {
             product: 'CGS20',
             side: 'BID'
          },
          success: function(reponse) {
             alert("deactivation of CGS20 was successful")
          }, 
          failure: function(reason) {
             alert("deactivation of CGS20 was a failure")
          },
       });
  
 */
function invoke(socket, invocation) {
   var errors = validateInvocation(invocation);    
   var count = errors.length;
   
   if(count <= 0) {
      var time = currentTime();
      var expiry = time + 20000;
      var sequence = calls++;
      var key = 'call_' + sequence + '_' + time;
      
      callbacks[key] = {            
         invocation: invocation,
         expiry: expiry
      }  
      var message = "invoke:";
      
      message += "service=";
      message += invocation.service;
      message += ",method=";
      message += invocation.method;
      message += ",key=";
      message += key;
      
      for(var argument in invocation.arguments) {
         if(invocation.arguments.hasOwnProperty(argument)) {
            message += ",";            
            message += "arguments{'"
            message += argument;
            message += "'}=";
            message += invocation.arguments[argument];
         }
      }
      socket.send(message); // service=pricebook,method=deactivate,key=4323@35346346,argument{'product'}=CGS20,argument{'side'}=BID
   } else {
      for(var i = 0; i < count; i++) {        
         invocation.failure(errors[i]);
      }
   }
}

function returnInvocation(socket, message) {
   var index = message.indexOf(':');
   var key = message.substring(0, index);
   var value = message.substring(index + 1);
   var callback = callbacks[key];
   
   if(callback != null) {
      var invocation = callback.invocation;
      
      if(invocation != null) {
         var object = JSON.parse(value); // parse the JSON response
         
         invocation.success(object);
         callbacks[key] = null;
      }
   }
}

function validateInvocation(invocation) {
   var service = invocation['service'];
   var method = invocation['method']
   var arguments = invocation['arguments'];      
   var success = invocation['success'];
   var failure = invocation['failure'];
   var errors = [];
   
   if(failure == undefined) {
      invocation['failure'] = failure = function(reason) {
         console.log(reason); /* default error goes to log */
      }
   }
   if(success == undefined) {
      invocation['success'] = success == function(result) {
         console.log(result); /* default success foes to log */
      }
   }   
   if(service == undefined) {
      errors.push("An RPC requires a 'service' attribute");     
   }
   if(method == undefined) {
      errors.push("An RPC requires a 'method' attribute");
   } 
   return errors;
}

function expireInvocations() {
   var time = currentTime();
   
   for(var key in callbacks) {
      if(callbacks.hasOwnProperty(key)) {
         var callback = callbacks[key];

         if(callback != null) {
            var invocation = callback['invocation'];       
                     
            if(callback.expiry < time) {
               expireInvocation(invocation);
               callbacks[key] = null; 
            }
         }
      }
   }
}

function expireInvocation(invocation) {   
   var service = invocation['service'];
   var method = invocation['method']
   var arguments = invocation['arguments']; 
   var reason = 'Time is up on ';
   var count = 0;
   
   reason += service;
   reason += ".";
   reason += method;
   reason += "(";
   
   for(var argument in arguments) {
      if(arguments.hasOwnProperty(argument)) {
         if(count++ > 0) {
            reason += ", ";
         }
         reason += argument;
         reason += ": ";
         reason += arguments[argument];
      }
   }
   reason += ")";
   invocation.failure(reason);   
}

registerModule("rpc", "RPC module: rpc.js", startInvocations, ["common", "socket"]);