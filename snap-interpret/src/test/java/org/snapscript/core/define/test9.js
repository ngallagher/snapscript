
module M{
	var x="blah blah";
	function x(t){
		System.err.println(t+": "+x);
	}
}

var b = new Blah(2222);
System.out.println(b.x);

class Blah{
	var x;
	new(x){
		this.x=x;
	}
	function dump(){
		System.out.println(x);
	}
}
b.dump();
M.x("hello world!");