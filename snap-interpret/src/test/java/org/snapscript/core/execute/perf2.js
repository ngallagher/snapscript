function fib(n) {
	if (n<2) {
		return 1;
	}
	return fib(n-1) + fib(n-2);
}
var result = fib(30);
out.println("fib(30)="+result);