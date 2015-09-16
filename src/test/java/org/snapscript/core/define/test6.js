class Point{
	var x;
	var y;
}
function makePoint(x, y) {
  var point = new Point();
  point.x = x;
  point.y = y;
  return point;
}

function makeArrayOfPoints(n){
  var array = [];
  var m = -1;
  for(var i = 0; i < n; i++){ 
    m = m * -1;
    array[i] = makePoint(m * i, m * -i);
  }
  return array;
}

function sumArrayOfPoints(array){
  var sum = makePoint(0, 0);
  for(var point in array){
    sum.x = sum.x + point.x;
    sum.y = sum.y + point.y;
  }
  return sum;
}

function checkResult(sum) {
  var x = sum.x;
  var y = sum.y;
  if (x != 50000 || y != -50000) {
    //throw "failed: x = " + x + ", y = " + y;
  }
}

var n = 100000;
var array = makeArrayOfPoints(n);
var start = System.currentTimeMillis();
for(var i = 0; i < 5; i++) {
  var sum = sumArrayOfPoints(array);
  checkResult(sum);
}
var end = System.currentTimeMillis();
System.err.println("Time to sum: " + (end - start));