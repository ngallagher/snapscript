

function run(t){
	t.run();
}

class Task with Runnable {
	
	function run(){
		System.out.println("test!!!!!!!");
	}
}

var task = new Task();

run(task);