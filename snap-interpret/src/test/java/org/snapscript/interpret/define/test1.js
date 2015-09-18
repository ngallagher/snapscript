function x(){
	return 1;
}
module Empty{
	
}

class TestCase {
	function runTests(){
		for(var f in this.class.functions){
			if(f.name.startsWith("test")){	
				out.println("Running.. "+f.name+"()");
				f.invocation.invoke(this,this);
			}
		}
	}
	function assertEquals(a,b){
		if(a!=b){
			throw "Assertion failed";
		}
	}
	function assertNotEquals(a,b){
		if(a==b){
			throw "Assertion failed";
		}
	}	
	function assertNotNull(a){
		if(a==null){
			throw "Assertion failed";
		}
	}
	function assertNull(a){
		if(a!=null){
			throw "Assertion failed";
		}
	}
	function assertTrue(a){
		if(a!=true){
			throw "Assertion failed";
		}
	}	
	function assertFalse(a){
		if(a!=false){
			throw "Assertion failed";
		}
	}	
}

class SimpleTest extends TestCase{
	
	function testNumbers(){
		var x=0;
		assertNotEquals(1d,21f);
		assertEquals(1d,1d);
		assertEquals(++x,1);
		assertTrue(x==1);
	}
	function testSomethingElse(){
		assertFalse(2==3);
	}	
}

var test = new SimpleTest();

test.runTests();


