try {
	out.println("try-outer");
	try {
		out.println("try-inner");
	}finally{
		out.println("finally-inner");
	}
}catch(e){
	out.println("caught-outer"+e);
}finally{
	out.println("finally-outer");
}