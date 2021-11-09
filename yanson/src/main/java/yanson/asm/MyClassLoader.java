package yanson.asm;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.util.Arrays;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 10:47
 */
public class MyClassLoader extends ClassLoader {

	public Class defineClass(String name, byte[] b) {
		return super.defineClass(name, b, 0, b.length);
	}

	public static void main(String[] args) throws Exception {
		MyClassLoader myClassLoader = new MyClassLoader();
		ClassReader cr = new ClassReader("");
		ClassWriter cw = new ClassWriter(cr, 0);
		VersionAdapter va = new VersionAdapter(cw);
		cr.accept(va, 0);
		byte[] bytes1 = cw.toByteArray();
		System.out.println(Arrays.toString(bytes1));
		ClassReader classReader = new ClassReader(bytes1);
		ClassWriter classWriter = new ClassWriter(classReader, 0);
		VersionAdapter versionAdapter = new VersionAdapter(classWriter);
		classReader.accept(versionAdapter, 0);
		byte[] bytes2 = classWriter.toByteArray();
		System.out.println(Arrays.toString(bytes2));
		Class clazz2 = myClassLoader.defineClass(null, bytes2);
		System.out.println(Arrays.equals(bytes1, bytes2));
	}

}
