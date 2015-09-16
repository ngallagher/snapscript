import static lang.Math.*;
import security.SecureRandom;

function gcd(a,b) {
  a = abs(a);
  b = abs(b);
 
  if (b > a) {
    var temp = a;
    a = b;
    b = temp; 
  }
 
  while (true) {
    a %= b;
    if (a == 0) { 
    	return b; 
    }
    b %= a;
    if (b == 0) { 
    	return a; 
    }
  }
}
var random = new SecureRandom();
var a = random.nextInt(40) + 1;
var b = random.nextInt(40) + 1;
var result = gcd(a, b);

out.println("gcd("+a+","+b+")="+result);