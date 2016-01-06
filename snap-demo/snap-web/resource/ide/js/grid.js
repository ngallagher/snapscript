var templates = {};
var records = {};
var schemas = {};
var indexes = {};
var footer = {};
var styles = {};
var tables = {};
var flashes = {};
var filters = {};

function startTable() {  
   setInterval(flashTables, 200);
   createRoute('T', deltaUpdate);
   createRoute('S', schemaUpdate);
}

function schemaUpdate(socket, message) {
   var parts = message.split('|');
   var structure = parts[0];
   var pair = structure.split("/");
   var address = pair[0];
   var key = pair[1];
   var styles = pair[2];
   var table = w2ui[address];

   if (table != null) {
      var minimum = parts.length - 1;
      var width = table.columns.length;
      
      if(styles.length > 0) {
         applyStyleSheet(styles);
      }
      if (width == 0) {
         schemas[table.name] = [];
         templates[table.name] = [];
         records[table.name] = [];
         flashes[table.name] = [];
         indexes[table.name] = key;
      }
      var schema = schemas[table.name];

      for (var i = 1; i < parts.length; i++) {
         var column = {};
         var interpolate = {};
         var part = parts[i];
         var values = part.split(',');
         var name = values[0];
         var highlight = values[4];

         column['name'] = name;
         column['caption'] = decodeValue(values[1]);
         column['template'] = decodeValue(values[2]);
         column['style'] = values[3];
         column['highlight'] = values[4];
         column['resizable'] = values[5] == 'true';
         column['sortable'] = values[6] == 'true';
         column['hidden'] = values[7] == 'true';
         column['control'] = values[8] == 'true';
         column['width'] = values[9];
         column['token'] = "{" + name + "}";
         column['pattern'] = new RegExp("{" + name + "}", "g");       
         column['interpolate'] = interpolate;
         interpolate['template'] = column.template.indexOf('{') != -1;        
         interpolate['style'] = column.style.indexOf('{') != -1;
         interpolate['highlight'] = column.highlight.indexOf('{') != -1; // if any is interpolated!

         schema[i - 1] = column;
      }
      if (width < minimum) {
         expandWidth(table);
         requestRefresh(socket, 'schemaUpdate');
      }
      clearTable(table);
      finishedLoading();
   }
}

function applyStyleSheet(source) {
   var list = source.split(',');
   var length = list.length;
   
   if(length > 0) {
      var style = document.createElement("style");
      var text = document.createTextNode("");
   
      style.appendChild(text);
      document.head.appendChild(style);   
   
      for(var i = 0; i < length; i++) {
         var entry = list[i];
         var pair = entry.split('=');
         
         if(pair.length > 0) {
            var selector = pair[0];
            var text = decodeValue(pair[1]);
            
            if("insertRule" in style.sheet) {
               style.sheet.insertRule(selector + "{" + text + "}", i);
            } else if("addRule" in style.sheet) {
               style.sheet.addRule(selector, text, i);
            }
            styles[selector] = text;
         }
      }
   }
}

function requestRefresh(socket, message) {
   socket.send('refresh:everything=true,message=' + message);
}

function deltaUpdate(socket, message) {
   var header = message.indexOf(':');
   var sequence = 0;
   var sent = 0;

   if (header > 0) {
      var token = message.substring(0, header);
      var parts = token.split('@');
      
      message = message.substring(header + 1);
      sequence = parts[0];
      sent = parts[1];
   }
   var parts = message.split('|');
   var address = parts[0];
   var table = w2ui[address];
   var schema = schemas[address];

   if (table != null) {
      var length = message.length;
      var start = currentTime();
      var total = 0;

      if (schema.length > 0) {
         total += updateTable(socket, table, parts, start);
      }
      var finish = currentTime();
      var model = records[table.name];
      var duration = finish - start;
      var height = records.length;

      reportStatus(socket, "success.png", height, length, total, duration, sequence, sent, address);
   }
}

function updateTable(socket, table, rows, time) {
   var deltas = [];
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
      deltas[count++] = change;
   }
   var model = records[table.name];      
   var height = model.length;
   var changes = [];
   
   if (height <= require) {
      expandHeight(table, require);
   }
   for (var i = 0; i < deltas.length; i++) {
      var index = deltas[i].index;
      var delta = deltas[i].delta;
      var cells = delta.split(',');

      if (cells.length > 0) {
         updateRow(socket, table, index, cells, time);
      }
      var template = templates[table.name][index];
      var record = records[table.name][index];
      
      if(acceptRow(table, record)) {
         changes.push(template.recid); // translate index for use with filters
      }
      total += cells.length;
   }
   if (total > 0) {
      drawTable(socket, table, changes, time);
      hideSpinner();
   }
   return total;
}

function selectCheckedRows(address, column) {
   var rows = records[address];
   var index = indexes[address];
   var size = rows.length;
   var matches = [];

   for(var i = 0; i < rows.length; i++) {
      var row = rows[i];
      var key = row[index];

      if(row.hasOwnProperty(column)) {
         var element = key + "_" + column;
         var control = document.getElementById(element);
         
         if (control != null) {
            if (control.type == 'checkbox') {
               if(control.checked) {
                  var copy = {};
                  
                  for(var key in row) {
                     if(row.hasOwnProperty(key)) {
                        copy[key] = row[key];
                     }
                  }
                  matches.push(copy);                       
               }                     
            }
         }
      }
   }  
   return matches;
}

function updateRow(socket, table, row, cells, time) {
   var record = records[table.name][row];
   var template = templates[table.name][row];
   var schema = schemas[table.name];
   var flash = flashes[table.name][row];  

   for (var i = 0; i < cells.length; i++) {
      var cell = cells[i].split('=');
      var column = cell[0];
      var value = cell[1];
      var style = schema[column];
      var decoded = decodeValue(value);

      record[style.name] = decoded;     
      flash.time[style.name] = time;
      flash.last = time;
   }
   interpolateRow(table, row, record, template, flash, time);
}

function drawTable(socket, table, changes, time) {
   var schema = schemas[table.name];
   var flash = flashes[table.name];
   var count = changes.length;   
   var width = schema.length;
   
   for (var i = 0; i < count; i++) {
      var row = changes[i];
      var template = templates[table.name][row];
      var record = records[table.name][row];

      if(acceptRow(table, record)) {
         for(var j = 0; j < width; j++) { // do a piggyback flash update
            var column = schema[j];
            var name = column.name;
            var update = flash[row].time[name];
            var highlight = flash[row].highlight[name]; // the higlight for this cell
            var normal = flash[row].normal[name]; // the normal style when flash has finished
            var elapsed = time - update;
            
            if(elapsed < 3000) {
               var index = Math.round(elapsed / 200);
               
               if(index < highlight.length) {               
                  template.style[j] = highlight[index]; 
               } else {
                  template.style[j] = normal;  
                  flash[row].time[name] = 0; // stop flashing it as its run out of styles and thus finished flashing
               }
            } else {
               template.style[j] = normal;                        
            }
         }        
         table.set(template.recid, template, true); // noRefresh=true do not refresh         
      }
   }
   reconcileTable(socket, table, changes); // avoid reflow

   for (var i = 0; i < count; i++) {
      var row = changes[i];
      var template = templates[table.name][row];
      var record = records[table.name][row];

      if(acceptRow(table, record)) {
         table.refreshRow(template.recid); // perform paint
      }
   }
}

function reconcileTable(socket, table, changes) {
   var count = changes.length;

   for (var i = 0; i < count; i++) {
      var row = changes[i];
      var template = templates[table.name][row];
      var record = records[table.name][row];

      if(acceptRow(table, record)) {
         reconcileRow(socket, table, row);
      }
   }
}

function reconcileRow(socket, table, row) {
   var template = templates[table.name][row];
   var schema = schemas[table.name];
   var element = table.get(template.recid);

   if(element != null) {
      for (var i = 0; i < schema.length; i++) {
         var style = schema[i];
         var name = style.name;
         var actual = element[name];
         var expect = template[name];
   
         if (actual != expect) {
            requestRefresh(socket, 'reconcileFailure');
         }
      }
   } else {
      console.log("no row in table="+table.name+" row="+row)
   }
}

function interpolateRow(table, row, record, template, flash, time) {
   var schema = schemas[table.name];
   var width = schema.length;

   for (var i = 0; i < width; i++) {
      var column = schema[i];
      var highlight = column.highlight;
      var style = column.style;
      var name = column.name;
      var text = column.template;

      if (column.control) {
         template[name] = interpolateControl(table, record, column, text); // add dynamic values      
      } else {
         if(column.interpolate.template) {
            template[name] = interpolateValue(table, record, column, text);
         } else {
            template[name] = text;
         }     
      }
      if(column.interpolate.style) {
         style = interpolateValue(table, record, column, style);         
      }
      var rule = styles[style]; // map from the class to the CSS style rules
      
      if(rule != undefined) {
         style = rule;
      } else {
         style = ''; // if no mapping exists!! 
      }
      var first = style; // the first style of a changed cell
      
      if(highlight.length > 0) {
         first = interpolateFlash(table, row, record, flash, column, style);
      } else {
         flash.normal[name] = style; 
         flash.highlight[name] = [style];
      }     
      var update = flash.time[name];
      
      if(update == time) {
         template.style[i] = first; // if just changed start off with flash
      } else {
         template.style[i] = style; // template starts with the default
      }
   }
}

function interpolateFlash(table, row, record, flash, column, style) {
   var highlight = column.highlight;
   var name = column.name;
   var classes = []; 
   var rules = []; 
   
   if(column.interpolate.highlight) {
      highlight = interpolateValue(table, record, column, highlight);
      classes = highlight.split('+');
   } else {
      classes = flash.original[name];
      
      if(classes.length == 0) { // only do it once for efficiency!!
         flash.original[name] = classes = highlight.split('+')
      }
   }
   var length = classes.length;
   
   if(length > 0) {
      var list = flash.highlight[name];
      
      if(list.length <= 0 || column.interpolate.highlight) { // if uninterpolated do it once only!!
         for(var i = 0; i < length; i++) {               
            var rule = styles[classes[i]]; // convert the class to the style rules
            
            if(rule != undefined) {
               rules[i] = rule; // convert from class to inline style
            } else {
               rules[i] = style; // no mapping
            }
         }     
         rules[length] = style; // set the last entry to be the normal style
         flash.highlight[name] = rules; // flash.highlight[i] = 'some list of CSS styles'
      }
      flash.normal[name] = style; // set the style when not flashing      
   } else {
      flash.normal[name] = style; // set the style when not flashing      
      flash.highlight[name] = [style]; // single value highlight
   }
   return flash.highlight[name][0]; // provide the first style in the flash
}

function interpolateControl(table, record, column, text) {
   if(column.interpolate.template) { // can be interpolate
      var name = column.name;
      var index = indexes[table.name]; // find the key
      var value = record[index];
      var key = value + "_" + name;
      var control = document.getElementById(key);
      
      if (control != null) {
         if (control.type == 'checkbox') {
            record['checked'] = control.checked ? 'checked' : ''; // add a checked setting 
         }
      }
      return interpolateValue(table, record, column, text);
   } 
   return text;   
}

function interpolateValue(table, record, column, text) {
   var source = interpolateToken(table, record, column, text); // most likely to match
   var schema = schemas[table.name];
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

function flashTables() {
   var time = currentTime();
   
   for(var name in tables) {
      var table = tables[name];
      var model = records[name];
      var height = model.length;
      
      if(height > 0) {
         var flash = flashes[name];
         var rows = [];
         var count = 0;
         
         for(var i = 0; i < height; i++) {
            var last = flash[i].last;
            var elapsed = time - last;
            
            if(elapsed < 3000) {
               rows[count++] = i;
            }
         }
         if(count > 0) {
            flashTable(table, flash, rows, time); // flash table with relevant rows only
         }
      }
   }
}

function flashTable(table, flash, rows, time) { // XXX FIX recid for filtering
   var schema = schemas[table.name];
   var changes = [];
   var count = 0; // row count

   for (var i = 0; i < rows.length; i++) {
      var row = rows[i];
      var flashing = 0; // how many cells need to be flashed?
      var change = {
         index : row,
         style : []
      }
      
      for(var j = 0; j < schema.length; j++) {        
         var column = schema[j];
         var name = column.name;
         var update = flash[row].time[name];
         var highlight = flash[row].highlight[name]; // the higlight for this cell
         var normal = flash[row].normal[name]; // the normal style when flash has finished
         var elapsed = time - update;
         
         if(elapsed < 3000) {
            var index = Math.round(elapsed / 200);
            
            if(index < highlight.length) {               
               change.style[j] = highlight[index];  
               flashing++; 
            } else {
               change.style[j] = normal;  
               flash[row].time[name] = 0; // stop flashing it as its run out of styles and thus finished flashing
            }
         } else {
            change.style[j] = normal;                        
         }
      }
      if(flashing <= 0) {
         flash[row].last = 0; // the whole row has finished flashing
      }
      changes[count++] = change;
   }
   refreshTable(table, schema, changes);   
}

function refreshTable(table, schema, changes) { // changes[] -> {row : x, columns : [a, b, c]}
   var count = changes.length;   
   var width = schema.length;
   var sort = false;
   
   for (var i = 0; i < count; i++) {
      var change = changes[i];
      var row = change.index;
      var template = templates[table.name][row];
      var record = records[table.name][row];
      
      for(var j = 0; j < width; j++) {
         template.style[j] = change.style[j]; // transfer styles to the template
      }
      if(template.update++ == 0) {
         sort = true;
      }
      if(acceptRow(table, record)) {
         table.set(template.recid, template, true); // noRefresh=true do not refresh
      }
   }
   //if(sort) {
   //  table.sort(); // this is very expensive!
   //}   
   for (var i = 0; i < count; i++) {
      var change = changes[i];
      var row = change.index;
      var template = templates[table.name][row];
      var record = templates[table.name][row];

      if(acceptRow(table, record)) {
         table.refreshRow(template.recid); // perform paint
      }
   }    
}

function filterTable(socket, name, match) { // match is text like security=AXX,company=DB
   var table = w2ui[name];
   
   if(table != null) {
      var values = match.split(',');
      var filter = {};
      var count = 0;
      
      for(var i = 0; i < values.length; i++) {
         var pair = values[i].split('=');
         var column = pair[0];
         var token = pair[1]; // this is the filter text/expression
         
         if(token.length > 0) {         
            filter[column] = token;
            count++;
         }
      }
      if(count > 0) {
         filters[name] = filter;
      } else {
         filters[name] = null;    
      }        
      buildTable(socket, table);      
   }
}

function buildTable(socket, table) {
   var rows = templates[table.name];
   var height = rows.length;

   if(height > 0) {
      table.clear();
   }
   var additions = [];
   var changes = [];
   var count = 0;

   for (var i = 0; i < height; i++) {
      var template = templates[table.name][i];
      var record = records[table.name][i];      
      
      if(acceptRow(table, record)) {
         var index = count++;
           
         additions[index] = template;
         template.recid = index; // need to change the record ids               
      }
      changes[i] = i
   }
   var time = currentTime();
   
   if(count > 0) {
      table.add(additions);
   }
   drawTable(socket, table, changes, time);
}

function acceptRow(table, record) {
   var filter = filters[table.name];
   
   if(filter != null) {
      for (var name in filter) {
         if(filter.hasOwnProperty(name)) {
            var token = filter[name];
            var text = record[name];
            
            if(text == null) {
               return false;
            }
            var index = text.indexOf(token);
               
            if(index == -1) {
               return false;
            }            
         }
      }
   }
   return true;
}

function clearTable(table) {
   var schema = schemas[table.name];
   var model = records[table.name];
   var height = model.length;
   var count = 0;

   for (var i = 0; i < height; i++) {
      var record = {
         style : []
      };
      var template = {
         recid : i,
         updates: 0,         
         style : []
      };
      var flash = {
         last : 0,
         time : {},
         original : {},         
         normal : {},
         highlight : {}           
      };     

      for (var j = 0; j < schema.length; j++) {
         var name = schema[j].name;

         template.style[j] = '';
         template[name] = '';
         record[name] = '';
         flash.time[name] = 0;
         flash.normal[name] = '';
         flash.original[name] = []; // basically a cache for uninterpolated highlights   
         flash.highlight[name] = [];          
      }      
      flashes[table.name][i] = flash;
      templates[table.name][i] = template;
      records[table.name][i] = record;
      table.set(template.recid, template, true); // noRefresh=true do not refresh
   }
   filters[table.name] = null;
   table.refresh();
}

function expandWidth(table) {
   var model = records[table.name];
   var schema = schemas[table.name];
   var width = table.columns.length;
   var height = model.length;

   for (var i = width; i < schema.length; i++) {
      var style = schema[i];
      var column = {};

      column['field'] = style.name;
      column['caption'] = style.caption;
      column['resizable'] = style.resizable;
      column['sortable'] = style.sortable;
      column['hidden'] = style.hidden;
      column['size'] = style.width + 'px';

      for (var j = 0; j < height; j++) {
         templates[table.name][i][name] = '';
         records[table.name][i][name] = '';
      }
      table.addColumn(column);
   }
}

function expandHeight(table, row) {
   var model = records[table.name];
   var schema = schemas[table.name];
   var height = model.length;
   var index = height;
   var additions = [];
   var count = 0;

   for (var i = height; i <= row; i++) {
      var record = {
         style : []         
      };
      var template = {
         recid : i,
         updates: 0,         
         style : []       
      };
      var flash = {
         last : 0,
         time : {},
         original : {},
         normal : {},
         highlight : {}          
      };
      
      for (var j = 0; j < schema.length; j++) {
         var name = schema[j].name;

         template[name] = '';
         record[name] = '';
         flash.time[name] = 0;
         flash.normal[name] = '';
         flash.original[name] = [];  // basically a cache for uninterpolated highlights         
         flash.highlight[name] = []; 
      }
      templates[table.name][i] = template;
      records[table.name][i] = record;
      flashes[table.name][i] = flash;
      
      if(acceptRow(table, record)) { // only add if not filtered
         additions[count++] = template;
         template.recid = index++;  // ensure recid is sequential even when filtered        
      }
   }
   if (count > 0) {
      table.add(additions); // add rows in batch
   }
   tables[table.name] = table;
}

function reportStatus(socket, status, height, delta, change, duration, sequence, sent, address) {
   var user = extractParameter("user");
   var image = '<img src="img/';

   image += status;
   image += '"';
   image += 'style="';
   image += ' max-width: 100%;';
   image += ' max-height: 25px;';
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
   if (socket.readyState == 1) {
      var message = "status";
      
      message += ":rows=" + height;
      message += ",change=" + change;
      message += ",duration=" + duration;
      message += ",sequence=" + sequence;
      message += ",sent=" + sent;
      message += ",address=" + address;
      message += ",user=" + user;
      
      socket.send(message);
   }
}

registerModule("grid", "Grid module: grid.js", startTable, ["common", "socket"]);