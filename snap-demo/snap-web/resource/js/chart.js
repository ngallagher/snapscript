var charts = {};  
var plots = {};

function startGraph() {  
   createRoute('G', graphUpdate);
}

function graphUpdate(socket, message) {      
   var start = message.indexOf('|');
   var length = message.length;
   
   if(start != -1) {
      var header = message.substring(0, start);
      var body = message.substring(start + 1, length - 1);
      var sections = header.split(':');
      
      if(sections.length > 0) {
         var data = sections[1];
         var list = data.split(',');
        
         for(var i = 0; i < list.length; i++) {
            createChart(list[i]);
         }         
      }
      graphPlot(body);
   }
}
var ccounter = 0;

function graphPlot(message) {
   var points = message.split('|');
   var changes = {};  
   
   for(var i = 0; i < points.length; i++) { // skip header!! which is at index 0
      var value = points[i];
      var point = createPoint(value);
      
      if(point != null) {
         var chart = charts[point.plot.owner];
         
         if(chart != undefined) {
            var plot = plots[point.plot.order];
                       
            if(validatePlot(plot.owner, plot.name)) { // should always be true
               var series = chart.get(plot.name);                  
               var count = plot.count++;
               var shift = count > plot.threshold;
                                 
               series.addPoint(
                     point.coordinate, 
                     false, // no redrawing
                     shift, // shift on size
                     false // no animation
               ); 
               changes[plot.owner] = chart;               
            }
         }
      }
   }
   for (var key in changes) {
      if (changes.hasOwnProperty(key)) {
         changes[key].redraw();
      }
   }
} 

function createChart(value) {
   var parts = value.split('/');
   var order = parts[0];
   var owner = parts[1];
   var title = parts[2];
   var plot = parts[3];
   var color = parts[4];
   var chart = charts[owner];
   
   if(chart == undefined) {
      chart = new Highcharts.Chart({
         title: {text: ''},      
         chart: {renderTo: owner, backgroundColor: '#f6f6f6', zoomType: 'x', type: 'line', marginTop: 50},
         plotOptions: {series: {animation: false}},         
         xAxis: {type: 'datetime', title: {text: ''}},
         yAxis: {title: {text: ''}},   
         tooltip: {headerFormat: '<b>{series.name}</b> ', pointFormat: '{point.y}'},         
         series: []
      });
      
      charts[owner] = chart;
   }
   var series = chart.get(plot);
   
   if(series == null) {
      chart.addSeries({
         name: title,
         id: plot,
         color: color
      }); 
      
      plots[order] = {
         owner: owner,
         order: order,
         name: plot,
         threshold: 2000,
         count: 0
      }
   }
   validatePlot(owner, plot);
}

function validatePlot(owner, plot) {
   var chart = $('#' + owner).highcharts();
   
   if(chart != undefined) {               
      var series = chart.get(plot);
      
      if(charts[owner] != chart) {
         return false;
      }
      if(series == undefined) {
         return false;
      }
      return true;
   }
   return false;
}

function createPoint(value) {
   var start = value.indexOf('(');
   var length = value.length;
   
   if(start != -1) {
      var order = value.substring(0, start);
      var data = value.substring(start + 1, length - 1);
      var coordinate = createCoordinate(data);
      var plot = plots[order];
      
      if(coordinate != null) {
         var point = {           
            plot: plot,
            coordinate: coordinate
         };         
         return point;
      }
   }
   return null;
}

function createCoordinate(value) {
   var parts = value.split(',');
   var point = {};    
   
   for(var i = 0; i < parts.length; i++) {
      var token = parts[i].toLowerCase();
      var pair = token.split('=');
      
      if(pair.length == 2) {
         var axis = pair[0];
         var data = decodeValue(pair[1]);
      
         if(data.length > 0) {
            var decimal = data.indexOf('.');
            
            if(decimal == -1) {
               point[axis] = parseInt(data);
            } else {
               point[axis] = parseFloat(data);
            }
         }
      }
   } 
   if(point.x != undefined && point.y != undefined) {
      return point;
   }
   return null;
}

registerModule("chart", "Chart module: chart.js", startGraph, ["common", "socket"]);