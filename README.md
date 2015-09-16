# SnapScript
Scripting language for various platforms, including Java (JVM), Android (Dalvik, ART), and Google Cloud (GAE). Syntax is a combination of TypeScript, ActionScript, and Scala.

This project contains a complete interpreter with the following components.

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
trait Service {
   function execute();
}

class Task with Service {

   function override execute() {
      // do something here...
   }
}
```
