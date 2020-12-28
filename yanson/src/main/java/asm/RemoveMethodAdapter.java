package asm;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import reflection.ClassUtil;
import test.BaseTypeVo;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.objectweb.asm.Opcodes.ASM4;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 17:41
 */
public class RemoveMethodAdapter extends ClassVisitor {

	private String methodName;
	private String methodDesc;

	public RemoveMethodAdapter(String methodName, String methodDesc) {
		super(ASM4);
		this.methodName = methodName;
		this.methodDesc = methodDesc;
	}

	public RemoveMethodAdapter(ClassVisitor classVisitor, String methodName, String methodDesc) {
		super(ASM4, classVisitor);
		this.methodName = methodName;
		this.methodDesc = methodDesc;
	}

	@Override
	public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {
		if (s.equals(methodName) && s1.equals(methodDesc)) {
			return null;
		}
		return cv.visitMethod(i, s, s1, s2, strings);
	}

	public static void main(String[] args) throws Exception {
		ClassReader classReader = new ClassReader(BaseTypeVo.class.getName());
		ClassWriter classWriter = new ClassWriter(classReader, 0);
		RemoveMethodAdapter removeMethodAdapter = new RemoveMethodAdapter(classWriter, "getString", "()Ljava/lang/String;");
		RemoveMethodAdapter removeMethodAdapter2 = new RemoveMethodAdapter(removeMethodAdapter, "setString", "(Ljava/lang/String;)Ljava/lang/String;");
		classReader.accept(removeMethodAdapter2, 0);
		byte[] bytes = classWriter.toByteArray();
		MyClassLoader myClassLoader = new MyClassLoader();
		Class clazz = myClassLoader.defineClass(null, bytes);
		Object o = clazz.newInstance();
		Method[] declaredMethods = o.getClass().getDeclaredMethods();
		for (Method method : declaredMethods) {
			System.out.println(method);
		}
	}
}
