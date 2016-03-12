function registerAlerts() {
   createRoute('ALERT', createAlert);
}

function createAlert(socket, type, text) {
   var message = JSON.parse(text);
   var text = message.message;
   
   w2alert('<table border="0" width="100%">'+
           '  <tr>'+
           '    <td>&nbsp;&nbsp</td>'+
           '    <td align="right"><img src="/img/warning.png"></td>'+
           '    <td align="left">&nbsp;&nbsp'+text+'</td>'+
           '  </tr>'+
           '</table>');

}

registerModule("alert", "Alert module: alert.js", registerAlerts, ["common", "socket"]);