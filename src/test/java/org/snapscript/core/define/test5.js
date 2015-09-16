//http://raid6.com.au/~onlyjob/posts/arena/
	
function test(){
	var str = "abcdefgh"+"efghefgh";
	var imax = 1024 / str.length() * 1024 * 4;
	
	var time = System.currentTimeMillis();
	System.out.println("exec.tm.sec\tstr.length\tallocated memory:free memory:memory used");
	var runtime = Runtime.getRuntime();
	System.out.println("0\t\t0\t\t"+runtime.totalMemory()/1024 +":"+ runtime.freeMemory()/1024+":"+(runtime.totalMemory()-runtime.freeMemory())/1024);
	
	var gstr = new StringBuilder();
	var i=0;
	var lngth;
	
	while (i++ < imax+1000) {
	    gstr.append(str);
	
	    var startIndx = gstr.indexOf("efgh");
	    while(startIndx != -1){
	        gstr.replace(startIndx, startIndx + 4, "____");
	        startIndx = gstr.indexOf("efgh", startIndx + 4);
	    }
	
		lngth=str.length()*i;
		if ((lngth % (1024*256)) == 0) {
		    System.out.println(((System.currentTimeMillis()-time)/1000)+"sec\t\t"+lngth/1024+"kb\t\t"+runtime.totalMemory()/1024+":"+runtime.freeMemory()/1024+":"+(runtime.totalMemory()-runtime.freeMemory())/1024);
		}
	}
}

//test();
