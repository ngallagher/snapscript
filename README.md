# SnapScript
Scripting language for various platforms, including Java (JVM), Android (Dalvik, ART), and Google Cloud (GAE). Syntax is a combination of TypeScript, ActionScript, and Scala.

This project is a work in progress at the moment, however it contains a complete interpreter with the following components.

1. Lexer
2. Parser
3. Assembler
4. Interpreter

Some code snippets

#### Function overloading

```javascript
function max(a, b) {
   if(a > b) {
      return a;
   }
   return b;
}

function max(a, b, c) {
   if(a > b) {
      return max(a, c);
   }
   return max(b, c);
}
```
#### Type system

```javascript
class Point{
   var x;
   var y;
   
   new(x, y) {
      this.x = x;
      this.y = y;
   }
}

function makePoint(x, y) {
   return new Point(x, y);
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
    throw "failed: x = " + x + ", y = " + y;
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
```
#### Optional type constraints

```javascript
trait Time {
   function getTime() {
      return System.currentTimeMillis();
   }
}

class Date with Time {
   var year: Integer;
   var month: Integer;
   var day: Integer;
   
   function new(year: Integer, month: Integer, day: Integer) {
      this.year = year;
      this.month = month;
      this.day = day;
   }
   
   function getTime() {
      // do something interesting here...
   }
}

var date = new Date(1977, 18, 11);

print(date);

function print(x: Integer) {
   System.out.println(x);
}

function print(x: Date) {
   System.err.println(x);
}
```
