File file = new File('/path/to/scripts'); // path used to load scripts
Store store = new FileStore(file);
Context context = new StoreContext(store);
Compiler compiler = new ResourceCompiler(context);
Map map = new HashMap();
Model model = new MapModel(map); // bindings for the script

map.put("blah", 10);
map.put("foo", "some text");
map.put("bar", 50);

Executable executable = compiler.compile('/run.snap');

executable.execute(model); // execute the script