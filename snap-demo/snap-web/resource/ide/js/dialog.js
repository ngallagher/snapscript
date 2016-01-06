var templates = [];
var records = [];
var schema = [];
var footer = {};
var index = null;

function startDialog() {  
   createRoute('T', deltaUpdate);
   createRoute('S', schemaUpdate);
}

function schemaUpdate(socket, message) {
   var parts = message.split('|');
   var structure = parts[0];
   var pair = structure.split("=");
   var address = pair[0];
   var key = pair[1];
   var table = document.getElementById(address);

   if (table != null) {
      var minimum = parts.length - 1;
      var width = schema.length;

      for (var i = 1; i < parts.length; i++) {
         var column = {};
         var interpolate = {};
         var part = parts[i];
         var values = part.split(',');
         var name = values[0];

         column['name'] = name;
         column['caption'] = decodeValue(values[1]);
         column['template'] = decodeValue(values[2]);
         column['style'] = decodeValue(values[3]);
         column['resizable'] = values[4] == 'true';
         column['sortable'] = values[5] == 'true';
         column['hidden'] = values[6] == 'true';
         column['control'] = values[7] == 'true';
         column['width'] = values[8];
         column['token'] = "{" + name + "}";
         column['pattern'] = new RegExp("{" + name + "}", "g");       
         column['interpolate'] = interpolate;
         interpolate['template'] = column.template.indexOf('{') != -1; 
         interpolate['style'] = column.style.indexOf('{') != -1;

         index = key;
         schema[i - 1] = column;
      }
      if (width < minimum) {
         expandWidth(table);
         requestRefresh(socket, 'schemaUpdate');
      }
   }
}

function requestRefresh(socket, message) {
   socket.send('refresh:everything=true,message=' + message);
}

function deltaUpdate(socket, message) {
   var header = message.indexOf(':');
   var sequence = 0;

   if (header > 0) {
      sequence = message.substring(0, header);
      message = message.substring(header + 1);
   }
   var parts = message.split('|');
   var address = parts[0];
   var table = document.getElementById(address);
   var start = currentTime();

   if (table != null) {
      var length = message.length;
      var total = 0;

      if (schema.length > 0) {
         total += updateTable(socket, table, parts);
      }
      var finish = currentTime();
      var duration = finish - start;
      var height = table.rows.length;

      reportStatus(socket, "success.png", height, length, total, duration, sequence, address);
   }
}

function updateTable(socket, table, rows) {
   var changes = [];
   var require = 0;
   var count = 0;
   var total = 0;

   for (var i = 1; i < rows.length; i++) {
      var update = rows[i];
      var pair = update.split(':');
      var row = parseInt(pair[0]);
      var change = {
         index : row,
         delta : pair[1]
      };

      if (require < change.index) {
         require = change.index;
      }
      changes[count++] = change;
   }
   var height = table.rows.length;

   if (height <= require) {
      expandHeight(table, require);
   }
   for (var i = 0; i < changes.length; i++) {
      var index = changes[i].index;
      var delta = changes[i].delta;
      var cells = delta.split(',');

      if (cells.length > 0) {
         updateRow(socket, table, index, cells);
      }
      total += cells.length;
      changes[i] = index;
   }
   if (total > 0) {
      drawTable(table, changes);
   }
   return total;
}

function updateRow(socket, table, row, cells) {
   var record = records[row];
   var template = templates[row];

   for (var i = 0; i < cells.length; i++) {
      var cell = cells[i].split('=');
      var column = cell[0];
      var value = cell[1];
      var style = schema[column];
      var decoded = decodeValue(value);

      record[style.name] = decoded;
   }
   interpolateRow(table, record, template);
}

function drawTable(table, changes) {
   var count = changes.length;

   for (var i = 0; i < count; i++) {
      var row = changes[i];
      var template = templates[row];

      drawRow(table, row, template);
   }
}

function drawRow(table, row, template) {
   var width = schema.length;

   for (var i = 0; i < width; i++) {
      var name = schema[i].name;
      var cell = table.rows[row].cells[i];

      cell.id = table.id + "_" + name + "_" + row;
      cell.style.cssText = template.style[i];
      cell.innerHTML = template[name];
   }
}

function interpolateRow(table, record, template) {
   var width = schema.length;

   for (var i = 0; i < width; i++) {
      var column = schema[i];
      var style = column.style;
      var name = column.name;
      var text = column.template;

      if (column.control) {
         template[name] = interpolateControl(table, record, column, text); // add dynamic values      
      } else {
         if(column.interpolate.template) {
            template[name] = interpolateCell(table, record, column, text);
         } else {
            template[name] = text;
         }     
      }
      if(column.interpolate.style) {
         template.style[i] = interpolateCell(table, record, column, style);
      } else {
         template.style[i] = style;
      }     
   }
}

function interpolateControl(table, record, column, text) {
   if(column.interpolate.template) { // can be interpolate
      var name = column.name;     
      var value = record[index]; // use the global variable 'index'
      var key = value + "_" + name;
      var control = document.getElementById(key);
      
      if (control != null) {
         if (control.type == 'checkbox') {
            record['checked'] = control.checked ? 'checked' : ''; // add a checked setting 
         }
      }
      return interpolateCell(table, record, column, text);
   } 
   return text;   
}

function interpolateCell(table, record, column, text) {
   var source = interpolateToken(table, record, column, text); // most likely to
                                                               // match
   var width = schema.length;

   for (var i = 0; i < 2; i++) { // find resursive references {{reference}}
      for (var j = 0; j < width; j++) {
         var index = source.indexOf('{'); // is there more work to do

         if (index == -1) {
            return source;
         }
         source = interpolateToken(table, record, schema[j], source);
      }
   }
   return source;
}

function interpolateToken(table, record, column, text) {
   var token = column.token;
   var index = text.indexOf(token); // can token be found?

   if (index == -1) {
      return text;
   }
   var pattern = column.pattern;
   var key = column.name;
   var value = record[key];

   return text.replace(pattern, value); // replace {reference} in text
}

function expandWidth(table) {
   var height = table.rows.length;
   var width = schema.length;

   for (var i = 0; i < height; i++) {
      var current = table.rows[i].cells.length;

      for (var j = current; j < width; j++) {
         table.rows[i].insertCell(j);
      }
   }
}

function expandHeight(table, row) {
   var height = table.rows.length;
   var width = schema.length;

   for (var i = height; i <= row; i++) {
      var record = {
         style : []
      };
      var template = {
         style : []
      };

      for (var j = 0; j < schema.length; j++) {
         var name = schema[j].name;

         template[name] = '';
         record[name] = '';
      }
      templates[i] = template;
      records[i] = record;
   }
   for (var i = height; i <= row; i++) {
      table.insertRow(i);

      for (var j = 0; j < width; j++) {
         table.rows[i].insertCell(j);
      }
   }
}

function reportStatus(socket, status, height, delta, change, duration, sequence, address) {
   var user = extractParameter("user");
   var image = '<img src="img/';

   image += status;
   image += '"';
   image += 'style="';
   image += ' max-width: 100%;';
   image += ' max-height: 100%;';
   image += ' padding-top: 4px;';
   image += ' padding-bottom: 4px;';
   image += ' padding-left: 4px;';
   image += ' padding-right: 8px;';
   image += '"/>';

   if (footer.connection != undefined) {
      footer.connection.innerHTML = image;
      footer.rows.innerHTML = height;
      footer.changes.innerHTML = change;
      footer.duration.innerHTML = duration;
   }
   socket.send("status:rows=" + height + ",change=" + change + ",duration=" + duration + ",sequence=" + sequence + ",address=" + address + ",user=" + user);
}

registerModule("dialog", "Dialog module: dialog.js", startDialog, ["common", "socket"]);