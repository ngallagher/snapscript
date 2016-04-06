function registerAlerts() {
    createRoute('ALERT', createAlert);
}
function createAlert(socket, type, text) {
    var message = JSON.parse(text);
    var text = message.message;
    w2alert('<table border="0" width="100%">' +
        '  <tr>' +
        '    <td>&nbsp;&nbsp</td>' +
        '    <td align="right"><img src="/img/warning.png"></td>' +
        '    <td align="left">&nbsp;&nbsp' + text + '</td>' +
        '  </tr>' +
        '</table>');
}
function createConfirmAlert(title, message, yesButton, noButton, yesCallback, noCallback) {
    var text = '<table border="0" width="100%">' +
        '  <tr>' +
        '    <td>&nbsp;&nbsp</td>' +
        '    <td align="right"><img src="/img/warning.png"></td>' +
        '    <td align="left">&nbsp;&nbsp' + message + '</td>' +
        '  </tr>' +
        '</table>';
    var options = {
        msg: text,
        title: title,
        width: 450,
        height: 220,
        yes_text: yesButton,
        yes_class: '',
        yes_style: '',
        yes_callBack: yesCallback,
        no_text: noButton,
        no_class: '',
        no_style: '',
        no_callBack: noCallback,
        callBack: null // common callBack
    };
    w2confirm(options);
}
registerModule("alert", "Alert module: alert.js", registerAlerts, ["common", "socket"]);
