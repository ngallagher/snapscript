
// should be caps because of packages!!
module Cars {
	
	System.err.println("XX");
	class Car{
		static const NAME='a';
		static const AGE=22;
		var variable = 10;
		const constant = 6;
		new(){}
		new(a){}
		drive(){
			System.out.println("Drive NAME="+NAME+" constant="+this.constant+" Car.NAME="+Car.NAME);
		}
		static call(){
			System.err.println("Static call to Car.call()");
		}
	}
	function dump(){
		System.err.println("dump");
	}
}
module Bikes{
	class MotorBike{
		static const X=233;
		static function check(a){
			System.err.println("Checking the bike a="+a);
		}
		static function check(a,b){
			System.err.println("Checking the bike a="+a+" b="+b);
		}
	}
}