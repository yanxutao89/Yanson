package asm;

import org.objectweb.asm.ClassWriter;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/26 13:39
 */
public class StubClassLoader extends ClassLoader {
	@Override
	protected Class findClass(String name)
			throws ClassNotFoundException {
		if (name.endsWith("_Stub")) {
			ClassWriter cw = new ClassWriter(0);
			byte[] b = cw.toByteArray();
			return defineClass(name, b, 0, b.length);
		}
		return super.findClass(name);
	}
}
