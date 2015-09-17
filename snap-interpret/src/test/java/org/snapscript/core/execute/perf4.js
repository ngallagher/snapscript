 
var delta=Math.sqrt(Math.ulp(1.0));
var infinity=Float.POSITIVE_INFINITY;

class Vec {
	var x;
	var y;
	var z;
	new(x2, y2, z2) { 
		this.x=x2; 
		this.y=y2; 
		this.z=z2; 
	}
}

function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

class Ray {
	var orig;
	var dir;
	new(o, d) { 
		this.orig=o; 
		this.dir=d; 
	}
}

class Hit {
	var lambda;
	var normal;
	new(l, n) { 
		this.lambda=l; 
		this.normal=n; 
	}
}

trait Scene {
	intersect(i, ray);
}

class Sphere with Scene {
	var center;
	var radius;

	new(c, r) { 
		this.center=c; 
		this.radius=r; 
	}

	raySphere(ray) {
	    var v = sub(center, ray.orig);
	    var b = dot(v, ray.dir);
		var disc = b*b - dot(v, v) + radius*radius;
	    if (disc < 0) return infinity;
	    var d = Math.sqrt(1.0*disc);
	    var t2 = b+d;
	    if (t2 < 0) return infinity;
	    var t1 = b-d;
	    return (t1 > 0 ? t1 : t2);
	}

	intersect(i, ray) {
		var l = raySphere(ray);
    	if (l >= i.lambda) return i;
    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
    	return new Hit(l, unitise(n));
	}
}

class Group with Scene {
	var bound;
	var objs;

	new(b) {
		this.bound = b;
		this.objs = new ArrayList();
	}

	intersect(i, ray) {
		var l = bound.raySphere(ray);
		if (l >= i.lambda) return i;
		var it = objs.listIterator(0);
		while (it.hasNext()) {
			var scene = it.next();
			i = scene.intersect(i, ray);
		}
		return i;
	}
}

function rayTrace(light, ray, scene) {
	var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
	if (i.lambda == infinity) return 0;
	var o = add(ray.orig, add(scale(i.lambda, ray.dir),
				  scale(delta, i.normal)));
	var g = dot(i.normal, light);
	if (g >= 0) return 0.0;
	var sray = new Ray(o, scale(-1, light));
	var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
	return (si.lambda == infinity ? -g : 0);
}

function create(level, c, r) {
	var sphere = new Sphere(c, r);
	if (level == 1) return sphere;
	var group = new Group(new Sphere(c, 3*r));
	group.objs.add(sphere);
	var rn = 3*r/Math.sqrt(12.0);
	for (var dz=-1; dz<=1; dz+=2)
	    for (var dx=-1; dx<=1; dx+=2) {
	    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
	    	group.objs.add(create(level-1, c2, r/2));
	    }
	return group;
}

function run(n, level, ss) {
	var scene = create(level, new Vec(0, -1, 0), 1);
	var out = System.out;
	out.print(("P5\n"+n+" "+n+"\n255\n"));
	for (var y=n-1; y>=0; --y)
		for (var x=0; x<n; ++x) {
			var g=0;
			for (var dx=0; dx<ss; ++dx)
				for (var dy=0; dy<ss; ++dy) {
					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
				}
            out.print(Math.floor(0.5+255.0*g/(ss*ss)));
            out.flush();
		}
	out.close();
}

module Ray {
    var delta=Math.sqrt(Math.ulp(1.0));
    var infinity=Float.POSITIVE_INFINITY;

    class Vec {
    	var x;
		var y;
		var z;
		new(x2, y2, z2) { 
			this.x=x2; 
			this.y=y2; 
			this.z=z2; 
		}
    }

    function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
    function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
    function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

    function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

    function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

    class Ray {
    	var orig;
    	var dir;
    	new(o, d) { 
    		this.orig=o; 
    		this.dir=d; 
    	}
    }

    class Hit {
    	var lambda;
    	var normal;
    	new(l, n) { 
    		this.lambda=l; 
    		this.normal=n; 
    	}
    }

    trait Scene {
    	intersect(i, ray);
    }

    class Sphere with Scene {
    	var center;
    	var radius;

    	new(c, r) { 
    		this.center=c; 
    		this.radius=r; 
    	}

    	raySphere(ray) {
		    var v = sub(center, ray.orig);
		    var b = dot(v, ray.dir);
			var disc = b*b - dot(v, v) + radius*radius;
		    if (disc < 0) return infinity;
		    var d = Math.sqrt(1.0*disc);
		    var t2 = b+d;
		    if (t2 < 0) return infinity;
		    var t1 = b-d;
		    return (t1 > 0 ? t1 : t2);
		}

    	intersect(i, ray) {
    		var l = raySphere(ray);
	    	if (l >= i.lambda) return i;
	    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
	    	return new Hit(l, unitise(n));
    	}
    }

    class Group with Scene {
    	var bound;
    	var objs;

    	new(b) {
    		this.bound = b;
    		this.objs = new ArrayList();
    	}

    	intersect(i, ray) {
    		var l = bound.raySphere(ray);
    		if (l >= i.lambda) return i;
    		var it = objs.listIterator(0);
    		while (it.hasNext()) {
    			var scene = it.next();
    			i = scene.intersect(i, ray);
    		}
    		return i;
    	}
    }

    function rayTrace(light, ray, scene) {
		var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
		if (i.lambda == infinity) return 0;
		var o = add(ray.orig, add(scale(i.lambda, ray.dir),
					  scale(delta, i.normal)));
		var g = dot(i.normal, light);
		if (g >= 0) return 0.0;
		var sray = new Ray(o, scale(-1, light));
		var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
		return (si.lambda == infinity ? -g : 0);
    }
    
    function create(level, c, r) {
		var sphere = new Sphere(c, r);
		if (level == 1) return sphere;
		var group = new Group(new Sphere(c, 3*r));
		group.objs.add(sphere);
		var rn = 3*r/Math.sqrt(12.0);
		for (var dz=-1; dz<=1; dz+=2)
		    for (var dx=-1; dx<=1; dx+=2) {
		    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
		    	group.objs.add(create(level-1, c2, r/2));
		    }
		return group;
    }

    function run(n, level, ss) {
    	var scene = create(level, new Vec(0, -1, 0), 1);
    	var out = System.out;
    	out.print(("P5\n"+n+" "+n+"\n255\n"));
    	for (var y=n-1; y>=0; --y)
    		for (var x=0; x<n; ++x) {
    			var g=0;
    			for (var dx=0; dx<ss; ++dx)
    				for (var dy=0; dy<ss; ++dy) {
    					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
    					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
    					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
    				}
                out.print(Math.floor(0.5+255.0*g/(ss*ss)));
                out.flush();
    		}
    	out.close();
    }

    run(100,200,4);
}
module Ray {
    var delta=Math.sqrt(Math.ulp(1.0));
    var infinity=Float.POSITIVE_INFINITY;

    class Vec {
    	var x;
		var y;
		var z;
		new(x2, y2, z2) { 
			this.x=x2; 
			this.y=y2; 
			this.z=z2; 
		}
    }

    function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
    function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
    function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

    function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

    function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

    class Ray {
    	var orig;
    	var dir;
    	new(o, d) { 
    		this.orig=o; 
    		this.dir=d; 
    	}
    }

    class Hit {
    	var lambda;
    	var normal;
    	new(l, n) { 
    		this.lambda=l; 
    		this.normal=n; 
    	}
    }

    trait Scene {
    	intersect(i, ray);
    }

    class Sphere with Scene {
    	var center;
    	var radius;

    	new(c, r) { 
    		this.center=c; 
    		this.radius=r; 
    	}

    	raySphere(ray) {
		    var v = sub(center, ray.orig);
		    var b = dot(v, ray.dir);
			var disc = b*b - dot(v, v) + radius*radius;
		    if (disc < 0) return infinity;
		    var d = Math.sqrt(1.0*disc);
		    var t2 = b+d;
		    if (t2 < 0) return infinity;
		    var t1 = b-d;
		    return (t1 > 0 ? t1 : t2);
		}

    	intersect(i, ray) {
    		var l = raySphere(ray);
	    	if (l >= i.lambda) return i;
	    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
	    	return new Hit(l, unitise(n));
    	}
    }

    class Group with Scene {
    	var bound;
    	var objs;

    	new(b) {
    		this.bound = b;
    		this.objs = new ArrayList();
    	}

    	intersect(i, ray) {
    		var l = bound.raySphere(ray);
    		if (l >= i.lambda) return i;
    		var it = objs.listIterator(0);
    		while (it.hasNext()) {
    			var scene = it.next();
    			i = scene.intersect(i, ray);
    		}
    		return i;
    	}
    }

    function rayTrace(light, ray, scene) {
		var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
		if (i.lambda == infinity) return 0;
		var o = add(ray.orig, add(scale(i.lambda, ray.dir),
					  scale(delta, i.normal)));
		var g = dot(i.normal, light);
		if (g >= 0) return 0.0;
		var sray = new Ray(o, scale(-1, light));
		var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
		return (si.lambda == infinity ? -g : 0);
    }
    
    function create(level, c, r) {
		var sphere = new Sphere(c, r);
		if (level == 1) return sphere;
		var group = new Group(new Sphere(c, 3*r));
		group.objs.add(sphere);
		var rn = 3*r/Math.sqrt(12.0);
		for (var dz=-1; dz<=1; dz+=2)
		    for (var dx=-1; dx<=1; dx+=2) {
		    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
		    	group.objs.add(create(level-1, c2, r/2));
		    }
		return group;
    }

    function run(n, level, ss) {
    	var scene = create(level, new Vec(0, -1, 0), 1);
    	var out = System.out;
    	out.print(("P5\n"+n+" "+n+"\n255\n"));
    	for (var y=n-1; y>=0; --y)
    		for (var x=0; x<n; ++x) {
    			var g=0;
    			for (var dx=0; dx<ss; ++dx)
    				for (var dy=0; dy<ss; ++dy) {
    					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
    					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
    					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
    				}
                out.print(Math.floor(0.5+255.0*g/(ss*ss)));
                out.flush();
    		}
    	out.close();
    }

    run(100,200,4);
}
module Ray {
    var delta=Math.sqrt(Math.ulp(1.0));
    var infinity=Float.POSITIVE_INFINITY;

    class Vec {
    	var x;
		var y;
		var z;
		new(x2, y2, z2) { 
			this.x=x2; 
			this.y=y2; 
			this.z=z2; 
		}
    }

    function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
    function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
    function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

    function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

    function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

    class Ray {
    	var orig;
    	var dir;
    	new(o, d) { 
    		this.orig=o; 
    		this.dir=d; 
    	}
    }

    class Hit {
    	var lambda;
    	var normal;
    	new(l, n) { 
    		this.lambda=l; 
    		this.normal=n; 
    	}
    }

    trait Scene {
    	intersect(i, ray);
    }

    class Sphere with Scene {
    	var center;
    	var radius;

    	new(c, r) { 
    		this.center=c; 
    		this.radius=r; 
    	}

    	raySphere(ray) {
		    var v = sub(center, ray.orig);
		    var b = dot(v, ray.dir);
			var disc = b*b - dot(v, v) + radius*radius;
		    if (disc < 0) return infinity;
		    var d = Math.sqrt(1.0*disc);
		    var t2 = b+d;
		    if (t2 < 0) return infinity;
		    var t1 = b-d;
		    return (t1 > 0 ? t1 : t2);
		}

    	intersect(i, ray) {
    		var l = raySphere(ray);
	    	if (l >= i.lambda) return i;
	    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
	    	return new Hit(l, unitise(n));
    	}
    }

    class Group with Scene {
    	var bound;
    	var objs;

    	new(b) {
    		this.bound = b;
    		this.objs = new ArrayList();
    	}

    	intersect(i, ray) {
    		var l = bound.raySphere(ray);
    		if (l >= i.lambda) return i;
    		var it = objs.listIterator(0);
    		while (it.hasNext()) {
    			var scene = it.next();
    			i = scene.intersect(i, ray);
    		}
    		return i;
    	}
    }

    function rayTrace(light, ray, scene) {
		var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
		if (i.lambda == infinity) return 0;
		var o = add(ray.orig, add(scale(i.lambda, ray.dir),
					  scale(delta, i.normal)));
		var g = dot(i.normal, light);
		if (g >= 0) return 0.0;
		var sray = new Ray(o, scale(-1, light));
		var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
		return (si.lambda == infinity ? -g : 0);
    }
    
    function create(level, c, r) {
		var sphere = new Sphere(c, r);
		if (level == 1) return sphere;
		var group = new Group(new Sphere(c, 3*r));
		group.objs.add(sphere);
		var rn = 3*r/Math.sqrt(12.0);
		for (var dz=-1; dz<=1; dz+=2)
		    for (var dx=-1; dx<=1; dx+=2) {
		    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
		    	group.objs.add(create(level-1, c2, r/2));
		    }
		return group;
    }

    function run(n, level, ss) {
    	var scene = create(level, new Vec(0, -1, 0), 1);
    	var out = System.out;
    	out.print(("P5\n"+n+" "+n+"\n255\n"));
    	for (var y=n-1; y>=0; --y)
    		for (var x=0; x<n; ++x) {
    			var g=0;
    			for (var dx=0; dx<ss; ++dx)
    				for (var dy=0; dy<ss; ++dy) {
    					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
    					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
    					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
    				}
                out.print(Math.floor(0.5+255.0*g/(ss*ss)));
                out.flush();
    		}
    	out.close();
    }

    run(100,200,4);
}
module Ray {
    var delta=Math.sqrt(Math.ulp(1.0));
    var infinity=Float.POSITIVE_INFINITY;

    class Vec {
    	var x;
		var y;
		var z;
		new(x2, y2, z2) { 
			this.x=x2; 
			this.y=y2; 
			this.z=z2; 
		}
    }

    function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
    function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
    function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

    function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

    function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

    class Ray {
    	var orig;
    	var dir;
    	new(o, d) { 
    		this.orig=o; 
    		this.dir=d; 
    	}
    }

    class Hit {
    	var lambda;
    	var normal;
    	new(l, n) { 
    		this.lambda=l; 
    		this.normal=n; 
    	}
    }

    trait Scene {
    	intersect(i, ray);
    }

    class Sphere with Scene {
    	var center;
    	var radius;

    	new(c, r) { 
    		this.center=c; 
    		this.radius=r; 
    	}

    	raySphere(ray) {
		    var v = sub(center, ray.orig);
		    var b = dot(v, ray.dir);
			var disc = b*b - dot(v, v) + radius*radius;
		    if (disc < 0) return infinity;
		    var d = Math.sqrt(1.0*disc);
		    var t2 = b+d;
		    if (t2 < 0) return infinity;
		    var t1 = b-d;
		    return (t1 > 0 ? t1 : t2);
		}

    	intersect(i, ray) {
    		var l = raySphere(ray);
	    	if (l >= i.lambda) return i;
	    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
	    	return new Hit(l, unitise(n));
    	}
    }

    class Group with Scene {
    	var bound;
    	var objs;

    	new(b) {
    		this.bound = b;
    		this.objs = new ArrayList();
    	}

    	intersect(i, ray) {
    		var l = bound.raySphere(ray);
    		if (l >= i.lambda) return i;
    		var it = objs.listIterator(0);
    		while (it.hasNext()) {
    			var scene = it.next();
    			i = scene.intersect(i, ray);
    		}
    		return i;
    	}
    }

    function rayTrace(light, ray, scene) {
		var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
		if (i.lambda == infinity) return 0;
		var o = add(ray.orig, add(scale(i.lambda, ray.dir),
					  scale(delta, i.normal)));
		var g = dot(i.normal, light);
		if (g >= 0) return 0.0;
		var sray = new Ray(o, scale(-1, light));
		var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
		return (si.lambda == infinity ? -g : 0);
    }
    
    function create(level, c, r) {
		var sphere = new Sphere(c, r);
		if (level == 1) return sphere;
		var group = new Group(new Sphere(c, 3*r));
		group.objs.add(sphere);
		var rn = 3*r/Math.sqrt(12.0);
		for (var dz=-1; dz<=1; dz+=2)
		    for (var dx=-1; dx<=1; dx+=2) {
		    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
		    	group.objs.add(create(level-1, c2, r/2));
		    }
		return group;
    }

    function run(n, level, ss) {
    	var scene = create(level, new Vec(0, -1, 0), 1);
    	var out = System.out;
    	out.print(("P5\n"+n+" "+n+"\n255\n"));
    	for (var y=n-1; y>=0; --y)
    		for (var x=0; x<n; ++x) {
    			var g=0;
    			for (var dx=0; dx<ss; ++dx)
    				for (var dy=0; dy<ss; ++dy) {
    					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
    					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
    					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
    				}
                out.print(Math.floor(0.5+255.0*g/(ss*ss)));
                out.flush();
    		}
    	out.close();
    }

    run(100,200,4);
}
module Ray {
    var delta=Math.sqrt(Math.ulp(1.0));
    var infinity=Float.POSITIVE_INFINITY;

    class Vec {
    	var x;
		var y;
		var z;
		new(x2, y2, z2) { 
			this.x=x2; 
			this.y=y2; 
			this.z=z2; 
		}
    }

    function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
    function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
    function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

    function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

    function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

    class Ray {
    	var orig;
    	var dir;
    	new(o, d) { 
    		this.orig=o; 
    		this.dir=d; 
    	}
    }

    class Hit {
    	var lambda;
    	var normal;
    	new(l, n) { 
    		this.lambda=l; 
    		this.normal=n; 
    	}
    }

    trait Scene {
    	intersect(i, ray);
    }

    class Sphere with Scene {
    	var center;
    	var radius;

    	new(c, r) { 
    		this.center=c; 
    		this.radius=r; 
    	}

    	raySphere(ray) {
		    var v = sub(center, ray.orig);
		    var b = dot(v, ray.dir);
			var disc = b*b - dot(v, v) + radius*radius;
		    if (disc < 0) return infinity;
		    var d = Math.sqrt(1.0*disc);
		    var t2 = b+d;
		    if (t2 < 0) return infinity;
		    var t1 = b-d;
		    return (t1 > 0 ? t1 : t2);
		}

    	intersect(i, ray) {
    		var l = raySphere(ray);
	    	if (l >= i.lambda) return i;
	    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
	    	return new Hit(l, unitise(n));
    	}
    }

    class Group with Scene {
    	var bound;
    	var objs;

    	new(b) {
    		this.bound = b;
    		this.objs = new ArrayList();
    	}

    	intersect(i, ray) {
    		var l = bound.raySphere(ray);
    		if (l >= i.lambda) return i;
    		var it = objs.listIterator(0);
    		while (it.hasNext()) {
    			var scene = it.next();
    			i = scene.intersect(i, ray);
    		}
    		return i;
    	}
    }

    function rayTrace(light, ray, scene) {
		var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
		if (i.lambda == infinity) return 0;
		var o = add(ray.orig, add(scale(i.lambda, ray.dir),
					  scale(delta, i.normal)));
		var g = dot(i.normal, light);
		if (g >= 0) return 0.0;
		var sray = new Ray(o, scale(-1, light));
		var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
		return (si.lambda == infinity ? -g : 0);
    }
    
    function create(level, c, r) {
		var sphere = new Sphere(c, r);
		if (level == 1) return sphere;
		var group = new Group(new Sphere(c, 3*r));
		group.objs.add(sphere);
		var rn = 3*r/Math.sqrt(12.0);
		for (var dz=-1; dz<=1; dz+=2)
		    for (var dx=-1; dx<=1; dx+=2) {
		    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
		    	group.objs.add(create(level-1, c2, r/2));
		    }
		return group;
    }

    function run(n, level, ss) {
    	var scene = create(level, new Vec(0, -1, 0), 1);
    	var out = System.out;
    	out.print(("P5\n"+n+" "+n+"\n255\n"));
    	for (var y=n-1; y>=0; --y)
    		for (var x=0; x<n; ++x) {
    			var g=0;
    			for (var dx=0; dx<ss; ++dx)
    				for (var dy=0; dy<ss; ++dy) {
    					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
    					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
    					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
    				}
                out.print(Math.floor(0.5+255.0*g/(ss*ss)));
                out.flush();
    		}
    	out.close();
    }

    run(100,200,4);
}
module Ray {
    var delta=Math.sqrt(Math.ulp(1.0));
    var infinity=Float.POSITIVE_INFINITY;

    class Vec {
    	var x;
		var y;
		var z;
		new(x2, y2, z2) { 
			this.x=x2; 
			this.y=y2; 
			this.z=z2; 
		}
    }

    function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
    function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
    function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

    function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

    function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

    class Ray {
    	var orig;
    	var dir;
    	new(o, d) { 
    		this.orig=o; 
    		this.dir=d; 
    	}
    }

    class Hit {
    	var lambda;
    	var normal;
    	new(l, n) { 
    		this.lambda=l; 
    		this.normal=n; 
    	}
    }

    trait Scene {
    	intersect(i, ray);
    }

    class Sphere with Scene {
    	var center;
    	var radius;

    	new(c, r) { 
    		this.center=c; 
    		this.radius=r; 
    	}

    	raySphere(ray) {
		    var v = sub(center, ray.orig);
		    var b = dot(v, ray.dir);
			var disc = b*b - dot(v, v) + radius*radius;
		    if (disc < 0) return infinity;
		    var d = Math.sqrt(1.0*disc);
		    var t2 = b+d;
		    if (t2 < 0) return infinity;
		    var t1 = b-d;
		    return (t1 > 0 ? t1 : t2);
		}

    	intersect(i, ray) {
    		var l = raySphere(ray);
	    	if (l >= i.lambda) return i;
	    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
	    	return new Hit(l, unitise(n));
    	}
    }

    class Group with Scene {
    	var bound;
    	var objs;

    	new(b) {
    		this.bound = b;
    		this.objs = new ArrayList();
    	}

    	intersect(i, ray) {
    		var l = bound.raySphere(ray);
    		if (l >= i.lambda) return i;
    		var it = objs.listIterator(0);
    		while (it.hasNext()) {
    			var scene = it.next();
    			i = scene.intersect(i, ray);
    		}
    		return i;
    	}
    }

    function rayTrace(light, ray, scene) {
		var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
		if (i.lambda == infinity) return 0;
		var o = add(ray.orig, add(scale(i.lambda, ray.dir),
					  scale(delta, i.normal)));
		var g = dot(i.normal, light);
		if (g >= 0) return 0.0;
		var sray = new Ray(o, scale(-1, light));
		var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
		return (si.lambda == infinity ? -g : 0);
    }
    
    function create(level, c, r) {
		var sphere = new Sphere(c, r);
		if (level == 1) return sphere;
		var group = new Group(new Sphere(c, 3*r));
		group.objs.add(sphere);
		var rn = 3*r/Math.sqrt(12.0);
		for (var dz=-1; dz<=1; dz+=2)
		    for (var dx=-1; dx<=1; dx+=2) {
		    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
		    	group.objs.add(create(level-1, c2, r/2));
		    }
		return group;
    }

    function run(n, level, ss) {
    	var scene = create(level, new Vec(0, -1, 0), 1);
    	var out = System.out;
    	out.print(("P5\n"+n+" "+n+"\n255\n"));
    	for (var y=n-1; y>=0; --y)
    		for (var x=0; x<n; ++x) {
    			var g=0;
    			for (var dx=0; dx<ss; ++dx)
    				for (var dy=0; dy<ss; ++dy) {
    					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
    					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
    					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
    				}
                out.print(Math.floor(0.5+255.0*g/(ss*ss)));
                out.flush();
    		}
    	out.close();
    }

    run(100,200,4);
}
module Ray {
    var delta=Math.sqrt(Math.ulp(1.0));
    var infinity=Float.POSITIVE_INFINITY;

    class Vec {
    	var x;
		var y;
		var z;
		new(x2, y2, z2) { 
			this.x=x2; 
			this.y=y2; 
			this.z=z2; 
		}
    }

    function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
    function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
    function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

    function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

    function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

    class Ray {
    	var orig;
    	var dir;
    	new(o, d) { 
    		this.orig=o; 
    		this.dir=d; 
    	}
    }

    class Hit {
    	var lambda;
    	var normal;
    	new(l, n) { 
    		this.lambda=l; 
    		this.normal=n; 
    	}
    }

    trait Scene {
    	intersect(i, ray);
    }

    class Sphere with Scene {
    	var center;
    	var radius;

    	new(c, r) { 
    		this.center=c; 
    		this.radius=r; 
    	}

    	raySphere(ray) {
		    var v = sub(center, ray.orig);
		    var b = dot(v, ray.dir);
			var disc = b*b - dot(v, v) + radius*radius;
		    if (disc < 0) return infinity;
		    var d = Math.sqrt(1.0*disc);
		    var t2 = b+d;
		    if (t2 < 0) return infinity;
		    var t1 = b-d;
		    return (t1 > 0 ? t1 : t2);
		}

    	intersect(i, ray) {
    		var l = raySphere(ray);
	    	if (l >= i.lambda) return i;
	    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
	    	return new Hit(l, unitise(n));
    	}
    }

    class Group with Scene {
    	var bound;
    	var objs;

    	new(b) {
    		this.bound = b;
    		this.objs = new ArrayList();
    	}

    	intersect(i, ray) {
    		var l = bound.raySphere(ray);
    		if (l >= i.lambda) return i;
    		var it = objs.listIterator(0);
    		while (it.hasNext()) {
    			var scene = it.next();
    			i = scene.intersect(i, ray);
    		}
    		return i;
    	}
    }

    function rayTrace(light, ray, scene) {
		var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
		if (i.lambda == infinity) return 0;
		var o = add(ray.orig, add(scale(i.lambda, ray.dir),
					  scale(delta, i.normal)));
		var g = dot(i.normal, light);
		if (g >= 0) return 0.0;
		var sray = new Ray(o, scale(-1, light));
		var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
		return (si.lambda == infinity ? -g : 0);
    }
    
    function create(level, c, r) {
		var sphere = new Sphere(c, r);
		if (level == 1) return sphere;
		var group = new Group(new Sphere(c, 3*r));
		group.objs.add(sphere);
		var rn = 3*r/Math.sqrt(12.0);
		for (var dz=-1; dz<=1; dz+=2)
		    for (var dx=-1; dx<=1; dx+=2) {
		    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
		    	group.objs.add(create(level-1, c2, r/2));
		    }
		return group;
    }

    function run(n, level, ss) {
    	var scene = create(level, new Vec(0, -1, 0), 1);
    	var out = System.out;
    	out.print(("P5\n"+n+" "+n+"\n255\n"));
    	for (var y=n-1; y>=0; --y)
    		for (var x=0; x<n; ++x) {
    			var g=0;
    			for (var dx=0; dx<ss; ++dx)
    				for (var dy=0; dy<ss; ++dy) {
    					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
    					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
    					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
    				}
                out.print(Math.floor(0.5+255.0*g/(ss*ss)));
                out.flush();
    		}
    	out.close();
    }

    run(100,200,4);
}
module Ray {
    var delta=Math.sqrt(Math.ulp(1.0));
    var infinity=Float.POSITIVE_INFINITY;

    class Vec {
    	var x;
		var y;
		var z;
		new(x2, y2, z2) { 
			this.x=x2; 
			this.y=y2; 
			this.z=z2; 
		}
    }

    function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
    function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
    function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

    function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

    function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

    class Ray {
    	var orig;
    	var dir;
    	new(o, d) { 
    		this.orig=o; 
    		this.dir=d; 
    	}
    }

    class Hit {
    	var lambda;
    	var normal;
    	new(l, n) { 
    		this.lambda=l; 
    		this.normal=n; 
    	}
    }

    trait Scene {
    	intersect(i, ray);
    }

    class Sphere with Scene {
    	var center;
    	var radius;

    	new(c, r) { 
    		this.center=c; 
    		this.radius=r; 
    	}

    	raySphere(ray) {
		    var v = sub(center, ray.orig);
		    var b = dot(v, ray.dir);
			var disc = b*b - dot(v, v) + radius*radius;
		    if (disc < 0) return infinity;
		    var d = Math.sqrt(1.0*disc);
		    var t2 = b+d;
		    if (t2 < 0) return infinity;
		    var t1 = b-d;
		    return (t1 > 0 ? t1 : t2);
		}

    	intersect(i, ray) {
    		var l = raySphere(ray);
	    	if (l >= i.lambda) return i;
	    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
	    	return new Hit(l, unitise(n));
    	}
    }

    class Group with Scene {
    	var bound;
    	var objs;

    	new(b) {
    		this.bound = b;
    		this.objs = new ArrayList();
    	}

    	intersect(i, ray) {
    		var l = bound.raySphere(ray);
    		if (l >= i.lambda) return i;
    		var it = objs.listIterator(0);
    		while (it.hasNext()) {
    			var scene = it.next();
    			i = scene.intersect(i, ray);
    		}
    		return i;
    	}
    }

    function rayTrace(light, ray, scene) {
		var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
		if (i.lambda == infinity) return 0;
		var o = add(ray.orig, add(scale(i.lambda, ray.dir),
					  scale(delta, i.normal)));
		var g = dot(i.normal, light);
		if (g >= 0) return 0.0;
		var sray = new Ray(o, scale(-1, light));
		var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
		return (si.lambda == infinity ? -g : 0);
    }
    
    function create(level, c, r) {
		var sphere = new Sphere(c, r);
		if (level == 1) return sphere;
		var group = new Group(new Sphere(c, 3*r));
		group.objs.add(sphere);
		var rn = 3*r/Math.sqrt(12.0);
		for (var dz=-1; dz<=1; dz+=2)
		    for (var dx=-1; dx<=1; dx+=2) {
		    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
		    	group.objs.add(create(level-1, c2, r/2));
		    }
		return group;
    }

    function run(n, level, ss) {
    	var scene = create(level, new Vec(0, -1, 0), 1);
    	var out = System.out;
    	out.print(("P5\n"+n+" "+n+"\n255\n"));
    	for (var y=n-1; y>=0; --y)
    		for (var x=0; x<n; ++x) {
    			var g=0;
    			for (var dx=0; dx<ss; ++dx)
    				for (var dy=0; dy<ss; ++dy) {
    					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
    					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
    					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
    				}
                out.print(Math.floor(0.5+255.0*g/(ss*ss)));
                out.flush();
    		}
    	out.close();
    }

    run(100,200,4);
}
module Ray {
    var delta=Math.sqrt(Math.ulp(1.0));
    var infinity=Float.POSITIVE_INFINITY;

    class Vec {
    	var x;
		var y;
		var z;
		new(x2, y2, z2) { 
			this.x=x2; 
			this.y=y2; 
			this.z=z2; 
		}
    }

    function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
    function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
    function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

    function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

    function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

    class Ray {
    	var orig;
    	var dir;
    	new(o, d) { 
    		this.orig=o; 
    		this.dir=d; 
    	}
    }

    class Hit {
    	var lambda;
    	var normal;
    	new(l, n) { 
    		this.lambda=l; 
    		this.normal=n; 
    	}
    }

    trait Scene {
    	intersect(i, ray);
    }

    class Sphere with Scene {
    	var center;
    	var radius;

    	new(c, r) { 
    		this.center=c; 
    		this.radius=r; 
    	}

    	raySphere(ray) {
		    var v = sub(center, ray.orig);
		    var b = dot(v, ray.dir);
			var disc = b*b - dot(v, v) + radius*radius;
		    if (disc < 0) return infinity;
		    var d = Math.sqrt(1.0*disc);
		    var t2 = b+d;
		    if (t2 < 0) return infinity;
		    var t1 = b-d;
		    return (t1 > 0 ? t1 : t2);
		}

    	intersect(i, ray) {
    		var l = raySphere(ray);
	    	if (l >= i.lambda) return i;
	    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
	    	return new Hit(l, unitise(n));
    	}
    }

    class Group with Scene {
    	var bound;
    	var objs;

    	new(b) {
    		this.bound = b;
    		this.objs = new ArrayList();
    	}

    	intersect(i, ray) {
    		var l = bound.raySphere(ray);
    		if (l >= i.lambda) return i;
    		var it = objs.listIterator(0);
    		while (it.hasNext()) {
    			var scene = it.next();
    			i = scene.intersect(i, ray);
    		}
    		return i;
    	}
    }

    function rayTrace(light, ray, scene) {
		var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
		if (i.lambda == infinity) return 0;
		var o = add(ray.orig, add(scale(i.lambda, ray.dir),
					  scale(delta, i.normal)));
		var g = dot(i.normal, light);
		if (g >= 0) return 0.0;
		var sray = new Ray(o, scale(-1, light));
		var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
		return (si.lambda == infinity ? -g : 0);
    }
    
    function create(level, c, r) {
		var sphere = new Sphere(c, r);
		if (level == 1) return sphere;
		var group = new Group(new Sphere(c, 3*r));
		group.objs.add(sphere);
		var rn = 3*r/Math.sqrt(12.0);
		for (var dz=-1; dz<=1; dz+=2)
		    for (var dx=-1; dx<=1; dx+=2) {
		    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
		    	group.objs.add(create(level-1, c2, r/2));
		    }
		return group;
    }

    function run(n, level, ss) {
    	var scene = create(level, new Vec(0, -1, 0), 1);
    	var out = System.out;
    	out.print(("P5\n"+n+" "+n+"\n255\n"));
    	for (var y=n-1; y>=0; --y)
    		for (var x=0; x<n; ++x) {
    			var g=0;
    			for (var dx=0; dx<ss; ++dx)
    				for (var dy=0; dy<ss; ++dy) {
    					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
    					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
    					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
    				}
                out.print(Math.floor(0.5+255.0*g/(ss*ss)));
                out.flush();
    		}
    	out.close();
    }

    run(100,200,4);
}
module Ray {
    var delta=Math.sqrt(Math.ulp(1.0));
    var infinity=Float.POSITIVE_INFINITY;

    class Vec {
    	var x;
		var y;
		var z;
		new(x2, y2, z2) { 
			this.x=x2; 
			this.y=y2; 
			this.z=z2; 
		}
    }

    function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
    function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
    function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

    function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

    function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

    class Ray {
    	var orig;
    	var dir;
    	new(o, d) { 
    		this.orig=o; 
    		this.dir=d; 
    	}
    }

    class Hit {
    	var lambda;
    	var normal;
    	new(l, n) { 
    		this.lambda=l; 
    		this.normal=n; 
    	}
    }

    trait Scene {
    	intersect(i, ray);
    }

    class Sphere with Scene {
    	var center;
    	var radius;

    	new(c, r) { 
    		this.center=c; 
    		this.radius=r; 
    	}

    	raySphere(ray) {
		    var v = sub(center, ray.orig);
		    var b = dot(v, ray.dir);
			var disc = b*b - dot(v, v) + radius*radius;
		    if (disc < 0) return infinity;
		    var d = Math.sqrt(1.0*disc);
		    var t2 = b+d;
		    if (t2 < 0) return infinity;
		    var t1 = b-d;
		    return (t1 > 0 ? t1 : t2);
		}

    	intersect(i, ray) {
    		var l = raySphere(ray);
	    	if (l >= i.lambda) return i;
	    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
	    	return new Hit(l, unitise(n));
    	}
    }

    class Group with Scene {
    	var bound;
    	var objs;

    	new(b) {
    		this.bound = b;
    		this.objs = new ArrayList();
    	}

    	intersect(i, ray) {
    		var l = bound.raySphere(ray);
    		if (l >= i.lambda) return i;
    		var it = objs.listIterator(0);
    		while (it.hasNext()) {
    			var scene = it.next();
    			i = scene.intersect(i, ray);
    		}
    		return i;
    	}
    }

    function rayTrace(light, ray, scene) {
		var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
		if (i.lambda == infinity) return 0;
		var o = add(ray.orig, add(scale(i.lambda, ray.dir),
					  scale(delta, i.normal)));
		var g = dot(i.normal, light);
		if (g >= 0) return 0.0;
		var sray = new Ray(o, scale(-1, light));
		var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
		return (si.lambda == infinity ? -g : 0);
    }
    
    function create(level, c, r) {
		var sphere = new Sphere(c, r);
		if (level == 1) return sphere;
		var group = new Group(new Sphere(c, 3*r));
		group.objs.add(sphere);
		var rn = 3*r/Math.sqrt(12.0);
		for (var dz=-1; dz<=1; dz+=2)
		    for (var dx=-1; dx<=1; dx+=2) {
		    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
		    	group.objs.add(create(level-1, c2, r/2));
		    }
		return group;
    }

    function run(n, level, ss) {
    	var scene = create(level, new Vec(0, -1, 0), 1);
    	var out = System.out;
    	out.print(("P5\n"+n+" "+n+"\n255\n"));
    	for (var y=n-1; y>=0; --y)
    		for (var x=0; x<n; ++x) {
    			var g=0;
    			for (var dx=0; dx<ss; ++dx)
    				for (var dy=0; dy<ss; ++dy) {
    					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
    					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
    					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
    				}
                out.print(Math.floor(0.5+255.0*g/(ss*ss)));
                out.flush();
    		}
    	out.close();
    }

    run(100,200,4);
}
module Ray {
    var delta=Math.sqrt(Math.ulp(1.0));
    var infinity=Float.POSITIVE_INFINITY;

    class Vec {
    	var x;
		var y;
		var z;
		new(x2, y2, z2) { 
			this.x=x2; 
			this.y=y2; 
			this.z=z2; 
		}
    }

    function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
    function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
    function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

    function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

    function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

    class Ray {
    	var orig;
    	var dir;
    	new(o, d) { 
    		this.orig=o; 
    		this.dir=d; 
    	}
    }

    class Hit {
    	var lambda;
    	var normal;
    	new(l, n) { 
    		this.lambda=l; 
    		this.normal=n; 
    	}
    }

    trait Scene {
    	intersect(i, ray);
    }

    class Sphere with Scene {
    	var center;
    	var radius;

    	new(c, r) { 
    		this.center=c; 
    		this.radius=r; 
    	}

    	raySphere(ray) {
		    var v = sub(center, ray.orig);
		    var b = dot(v, ray.dir);
			var disc = b*b - dot(v, v) + radius*radius;
		    if (disc < 0) return infinity;
		    var d = Math.sqrt(1.0*disc);
		    var t2 = b+d;
		    if (t2 < 0) return infinity;
		    var t1 = b-d;
		    return (t1 > 0 ? t1 : t2);
		}

    	intersect(i, ray) {
    		var l = raySphere(ray);
	    	if (l >= i.lambda) return i;
	    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
	    	return new Hit(l, unitise(n));
    	}
    }

    class Group with Scene {
    	var bound;
    	var objs;

    	new(b) {
    		this.bound = b;
    		this.objs = new ArrayList();
    	}

    	intersect(i, ray) {
    		var l = bound.raySphere(ray);
    		if (l >= i.lambda) return i;
    		var it = objs.listIterator(0);
    		while (it.hasNext()) {
    			var scene = it.next();
    			i = scene.intersect(i, ray);
    		}
    		return i;
    	}
    }

    function rayTrace(light, ray, scene) {
		var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
		if (i.lambda == infinity) return 0;
		var o = add(ray.orig, add(scale(i.lambda, ray.dir),
					  scale(delta, i.normal)));
		var g = dot(i.normal, light);
		if (g >= 0) return 0.0;
		var sray = new Ray(o, scale(-1, light));
		var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
		return (si.lambda == infinity ? -g : 0);
    }
    
    function create(level, c, r) {
		var sphere = new Sphere(c, r);
		if (level == 1) return sphere;
		var group = new Group(new Sphere(c, 3*r));
		group.objs.add(sphere);
		var rn = 3*r/Math.sqrt(12.0);
		for (var dz=-1; dz<=1; dz+=2)
		    for (var dx=-1; dx<=1; dx+=2) {
		    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
		    	group.objs.add(create(level-1, c2, r/2));
		    }
		return group;
    }

    function run(n, level, ss) {
    	var scene = create(level, new Vec(0, -1, 0), 1);
    	var out = System.out;
    	out.print(("P5\n"+n+" "+n+"\n255\n"));
    	for (var y=n-1; y>=0; --y)
    		for (var x=0; x<n; ++x) {
    			var g=0;
    			for (var dx=0; dx<ss; ++dx)
    				for (var dy=0; dy<ss; ++dy) {
    					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
    					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
    					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
    				}
                out.print(Math.floor(0.5+255.0*g/(ss*ss)));
                out.flush();
    		}
    	out.close();
    }

    run(100,200,4);
}
module Ray {
    var delta=Math.sqrt(Math.ulp(1.0));
    var infinity=Float.POSITIVE_INFINITY;

    class Vec {
    	var x;
		var y;
		var z;
		new(x2, y2, z2) { 
			this.x=x2; 
			this.y=y2; 
			this.z=z2; 
		}
    }

    function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
    function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
    function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

    function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

    function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

    class Ray {
    	var orig;
    	var dir;
    	new(o, d) { 
    		this.orig=o; 
    		this.dir=d; 
    	}
    }

    class Hit {
    	var lambda;
    	var normal;
    	new(l, n) { 
    		this.lambda=l; 
    		this.normal=n; 
    	}
    }

    trait Scene {
    	intersect(i, ray);
    }

    class Sphere with Scene {
    	var center;
    	var radius;

    	new(c, r) { 
    		this.center=c; 
    		this.radius=r; 
    	}

    	raySphere(ray) {
		    var v = sub(center, ray.orig);
		    var b = dot(v, ray.dir);
			var disc = b*b - dot(v, v) + radius*radius;
		    if (disc < 0) return infinity;
		    var d = Math.sqrt(1.0*disc);
		    var t2 = b+d;
		    if (t2 < 0) return infinity;
		    var t1 = b-d;
		    return (t1 > 0 ? t1 : t2);
		}

    	intersect(i, ray) {
    		var l = raySphere(ray);
	    	if (l >= i.lambda) return i;
	    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
	    	return new Hit(l, unitise(n));
    	}
    }

    class Group with Scene {
    	var bound;
    	var objs;

    	new(b) {
    		this.bound = b;
    		this.objs = new ArrayList();
    	}

    	intersect(i, ray) {
    		var l = bound.raySphere(ray);
    		if (l >= i.lambda) return i;
    		var it = objs.listIterator(0);
    		while (it.hasNext()) {
    			var scene = it.next();
    			i = scene.intersect(i, ray);
    		}
    		return i;
    	}
    }

    function rayTrace(light, ray, scene) {
		var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
		if (i.lambda == infinity) return 0;
		var o = add(ray.orig, add(scale(i.lambda, ray.dir),
					  scale(delta, i.normal)));
		var g = dot(i.normal, light);
		if (g >= 0) return 0.0;
		var sray = new Ray(o, scale(-1, light));
		var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
		return (si.lambda == infinity ? -g : 0);
    }
    
    function create(level, c, r) {
		var sphere = new Sphere(c, r);
		if (level == 1) return sphere;
		var group = new Group(new Sphere(c, 3*r));
		group.objs.add(sphere);
		var rn = 3*r/Math.sqrt(12.0);
		for (var dz=-1; dz<=1; dz+=2)
		    for (var dx=-1; dx<=1; dx+=2) {
		    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
		    	group.objs.add(create(level-1, c2, r/2));
		    }
		return group;
    }

    function run(n, level, ss) {
    	var scene = create(level, new Vec(0, -1, 0), 1);
    	var out = System.out;
    	out.print(("P5\n"+n+" "+n+"\n255\n"));
    	for (var y=n-1; y>=0; --y)
    		for (var x=0; x<n; ++x) {
    			var g=0;
    			for (var dx=0; dx<ss; ++dx)
    				for (var dy=0; dy<ss; ++dy) {
    					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
    					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
    					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
    				}
                out.print(Math.floor(0.5+255.0*g/(ss*ss)));
                out.flush();
    		}
    	out.close();
    }

    run(100,200,4);
}
module Ray {
    var delta=Math.sqrt(Math.ulp(1.0));
    var infinity=Float.POSITIVE_INFINITY;

    class Vec {
    	var x;
		var y;
		var z;
		new(x2, y2, z2) { 
			this.x=x2; 
			this.y=y2; 
			this.z=z2; 
		}
    }

    function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
    function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
    function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

    function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

    function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

    class Ray {
    	var orig;
    	var dir;
    	new(o, d) { 
    		this.orig=o; 
    		this.dir=d; 
    	}
    }

    class Hit {
    	var lambda;
    	var normal;
    	new(l, n) { 
    		this.lambda=l; 
    		this.normal=n; 
    	}
    }

    trait Scene {
    	intersect(i, ray);
    }

    class Sphere with Scene {
    	var center;
    	var radius;

    	new(c, r) { 
    		this.center=c; 
    		this.radius=r; 
    	}

    	raySphere(ray) {
		    var v = sub(center, ray.orig);
		    var b = dot(v, ray.dir);
			var disc = b*b - dot(v, v) + radius*radius;
		    if (disc < 0) return infinity;
		    var d = Math.sqrt(1.0*disc);
		    var t2 = b+d;
		    if (t2 < 0) return infinity;
		    var t1 = b-d;
		    return (t1 > 0 ? t1 : t2);
		}

    	intersect(i, ray) {
    		var l = raySphere(ray);
	    	if (l >= i.lambda) return i;
	    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
	    	return new Hit(l, unitise(n));
    	}
    }

    class Group with Scene {
    	var bound;
    	var objs;

    	new(b) {
    		this.bound = b;
    		this.objs = new ArrayList();
    	}

    	intersect(i, ray) {
    		var l = bound.raySphere(ray);
    		if (l >= i.lambda) return i;
    		var it = objs.listIterator(0);
    		while (it.hasNext()) {
    			var scene = it.next();
    			i = scene.intersect(i, ray);
    		}
    		return i;
    	}
    }

    function rayTrace(light, ray, scene) {
		var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
		if (i.lambda == infinity) return 0;
		var o = add(ray.orig, add(scale(i.lambda, ray.dir),
					  scale(delta, i.normal)));
		var g = dot(i.normal, light);
		if (g >= 0) return 0.0;
		var sray = new Ray(o, scale(-1, light));
		var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
		return (si.lambda == infinity ? -g : 0);
    }
    
    function create(level, c, r) {
		var sphere = new Sphere(c, r);
		if (level == 1) return sphere;
		var group = new Group(new Sphere(c, 3*r));
		group.objs.add(sphere);
		var rn = 3*r/Math.sqrt(12.0);
		for (var dz=-1; dz<=1; dz+=2)
		    for (var dx=-1; dx<=1; dx+=2) {
		    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
		    	group.objs.add(create(level-1, c2, r/2));
		    }
		return group;
    }

    function run(n, level, ss) {
    	var scene = create(level, new Vec(0, -1, 0), 1);
    	var out = System.out;
    	out.print(("P5\n"+n+" "+n+"\n255\n"));
    	for (var y=n-1; y>=0; --y)
    		for (var x=0; x<n; ++x) {
    			var g=0;
    			for (var dx=0; dx<ss; ++dx)
    				for (var dy=0; dy<ss; ++dy) {
    					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
    					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
    					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
    				}
                out.print(Math.floor(0.5+255.0*g/(ss*ss)));
                out.flush();
    		}
    	out.close();
    }

    run(100,200,4);
}
module Ray {
    var delta=Math.sqrt(Math.ulp(1.0));
    var infinity=Float.POSITIVE_INFINITY;

    class Vec {
    	var x;
		var y;
		var z;
		new(x2, y2, z2) { 
			this.x=x2; 
			this.y=y2; 
			this.z=z2; 
		}
    }

    function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
    function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
    function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

    function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

    function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

    class Ray {
    	var orig;
    	var dir;
    	new(o, d) { 
    		this.orig=o; 
    		this.dir=d; 
    	}
    }

    class Hit {
    	var lambda;
    	var normal;
    	new(l, n) { 
    		this.lambda=l; 
    		this.normal=n; 
    	}
    }

    trait Scene {
    	intersect(i, ray);
    }

    class Sphere with Scene {
    	var center;
    	var radius;

    	new(c, r) { 
    		this.center=c; 
    		this.radius=r; 
    	}

    	raySphere(ray) {
		    var v = sub(center, ray.orig);
		    var b = dot(v, ray.dir);
			var disc = b*b - dot(v, v) + radius*radius;
		    if (disc < 0) return infinity;
		    var d = Math.sqrt(1.0*disc);
		    var t2 = b+d;
		    if (t2 < 0) return infinity;
		    var t1 = b-d;
		    return (t1 > 0 ? t1 : t2);
		}

    	intersect(i, ray) {
    		var l = raySphere(ray);
	    	if (l >= i.lambda) return i;
	    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
	    	return new Hit(l, unitise(n));
    	}
    }

    class Group with Scene {
    	var bound;
    	var objs;

    	new(b) {
    		this.bound = b;
    		this.objs = new ArrayList();
    	}

    	intersect(i, ray) {
    		var l = bound.raySphere(ray);
    		if (l >= i.lambda) return i;
    		var it = objs.listIterator(0);
    		while (it.hasNext()) {
    			var scene = it.next();
    			i = scene.intersect(i, ray);
    		}
    		return i;
    	}
    }

    function rayTrace(light, ray, scene) {
		var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
		if (i.lambda == infinity) return 0;
		var o = add(ray.orig, add(scale(i.lambda, ray.dir),
					  scale(delta, i.normal)));
		var g = dot(i.normal, light);
		if (g >= 0) return 0.0;
		var sray = new Ray(o, scale(-1, light));
		var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
		return (si.lambda == infinity ? -g : 0);
    }
    
    function create(level, c, r) {
		var sphere = new Sphere(c, r);
		if (level == 1) return sphere;
		var group = new Group(new Sphere(c, 3*r));
		group.objs.add(sphere);
		var rn = 3*r/Math.sqrt(12.0);
		for (var dz=-1; dz<=1; dz+=2)
		    for (var dx=-1; dx<=1; dx+=2) {
		    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
		    	group.objs.add(create(level-1, c2, r/2));
		    }
		return group;
    }

    function run(n, level, ss) {
    	var scene = create(level, new Vec(0, -1, 0), 1);
    	var out = System.out;
    	out.print(("P5\n"+n+" "+n+"\n255\n"));
    	for (var y=n-1; y>=0; --y)
    		for (var x=0; x<n; ++x) {
    			var g=0;
    			for (var dx=0; dx<ss; ++dx)
    				for (var dy=0; dy<ss; ++dy) {
    					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
    					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
    					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
    				}
                out.print(Math.floor(0.5+255.0*g/(ss*ss)));
                out.flush();
    		}
    	out.close();
    }

    run(100,200,4);
}
module Ray {
    var delta=Math.sqrt(Math.ulp(1.0));
    var infinity=Float.POSITIVE_INFINITY;

    class Vec {
    	var x;
		var y;
		var z;
		new(x2, y2, z2) { 
			this.x=x2; 
			this.y=y2; 
			this.z=z2; 
		}
    }

    function add(a, b) { return new Vec(a.x+b.x, a.y+b.y, a.z+b.z); }
    function sub(a, b) { return new Vec(a.x-b.x, a.y-b.y, a.z-b.z); }
    function scale(s, a) { return new Vec(s*a.x, s*a.y, s*a.z); }

    function dot(a, b) { return a.x*b.x + a.y*b.y + a.z*b.z; }

    function unitise(a) { return scale(1 / Math.sqrt(1.0*dot(a, a)), a); }

    class Ray {
    	var orig;
    	var dir;
    	new(o, d) { 
    		this.orig=o; 
    		this.dir=d; 
    	}
    }

    class Hit {
    	var lambda;
    	var normal;
    	new(l, n) { 
    		this.lambda=l; 
    		this.normal=n; 
    	}
    }

    trait Scene {
    	intersect(i, ray);
    }

    class Sphere with Scene {
    	var center;
    	var radius;

    	new(c, r) { 
    		this.center=c; 
    		this.radius=r; 
    	}

    	raySphere(ray) {
		    var v = sub(center, ray.orig);
		    var b = dot(v, ray.dir);
			var disc = b*b - dot(v, v) + radius*radius;
		    if (disc < 0) return infinity;
		    var d = Math.sqrt(1.0*disc);
		    var t2 = b+d;
		    if (t2 < 0) return infinity;
		    var t1 = b-d;
		    return (t1 > 0 ? t1 : t2);
		}

    	intersect(i, ray) {
    		var l = raySphere(ray);
	    	if (l >= i.lambda) return i;
	    	var n = add(ray.orig, sub(scale(l, ray.dir), center));
	    	return new Hit(l, unitise(n));
    	}
    }

    class Group with Scene {
    	var bound;
    	var objs;

    	new(b) {
    		this.bound = b;
    		this.objs = new ArrayList();
    	}

    	intersect(i, ray) {
    		var l = bound.raySphere(ray);
    		if (l >= i.lambda) return i;
    		var it = objs.listIterator(0);
    		while (it.hasNext()) {
    			var scene = it.next();
    			i = scene.intersect(i, ray);
    		}
    		return i;
    	}
    }

    function rayTrace(light, ray, scene) {
		var i = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), ray);
		if (i.lambda == infinity) return 0;
		var o = add(ray.orig, add(scale(i.lambda, ray.dir),
					  scale(delta, i.normal)));
		var g = dot(i.normal, light);
		if (g >= 0) return 0.0;
		var sray = new Ray(o, scale(-1, light));
		var si = scene.intersect(new Hit(infinity, new Vec(0, 0, 0)), sray);
		return (si.lambda == infinity ? -g : 0);
    }
    
    function create(level, c, r) {
		var sphere = new Sphere(c, r);
		if (level == 1) return sphere;
		var group = new Group(new Sphere(c, 3*r));
		group.objs.add(sphere);
		var rn = 3*r/Math.sqrt(12.0);
		for (var dz=-1; dz<=1; dz+=2)
		    for (var dx=-1; dx<=1; dx+=2) {
		    	var c2 = new Vec(c.x+dx*rn, c.y+rn, c.z+dz*rn);
		    	group.objs.add(create(level-1, c2, r/2));
		    }
		return group;
    }

    function run(n, level, ss) {
    	var scene = create(level, new Vec(0, -1, 0), 1);
    	var out = System.out;
    	out.print(("P5\n"+n+" "+n+"\n255\n"));
    	for (var y=n-1; y>=0; --y)
    		for (var x=0; x<n; ++x) {
    			var g=0;
    			for (var dx=0; dx<ss; ++dx)
    				for (var dy=0; dy<ss; ++dy) {
    					var d = new Vec(x+dx*1.0/ss-n/2.0, y+dy*1.0/ss-n/2.0, n);
    					var ray = new Ray(new Vec(0, 0, -4), unitise(d));
    					g += rayTrace(unitise(new Vec(-1, -3, 2)), ray, scene);
    				}
                out.print(Math.floor(0.5+255.0*g/(ss*ss)));
                out.flush();
    		}
    	out.close();
    }

    run(100,200,4);
}

