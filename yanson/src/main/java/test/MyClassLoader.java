package test;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/26 13:35
 */
public class MyClassLoader extends ClassLoader {

	public Class defineClass(String name, byte[] b) {
		return defineClass(name, b, 0, b.length);
	}

	public static void main(String[] args) throws Exception {
		MyClassLoader myClassLoader = new MyClassLoader();
		myClassLoader.defineClass("pkg.Comparable", ClassCreator.createClassWithByteArray());
	}
}