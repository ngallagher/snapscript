	import nio.file.Files;
	import nio.file.Paths;
	import nio.charset.Charset;


	function r(path){
		return Files.readAllLines(Paths.get(path),Charset.forName("UTF-8"));
	}
