

function run(t){
   t.run();
}

function start(t){
   t.start();
}

class Task with Runnable {
   
   function run(){
      System.out.println("test!!!!!!!"+Thread.currentThread().getName());
   }
}


var task = new Task();
var thread = new Thread(task);

run(thread);
start(thread);