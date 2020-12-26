package asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM4;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/26 13:54
 */
public class RemoveMethodAdapter extends ClassVisitor {
	private String mName;
	private String mDesc;

	public RemoveMethodAdapter(ClassVisitor cv, String mName, String mDesc) {
		super(ASM4, cv);
		this.mName = mName;
		this.mDesc = mDesc;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if (name.equals(mName) && desc.equals(mDesc)) {
			return null; // do not delegate to next visitor -> this removes the method
		}
		return cv.visitMethod(access, name, desc, signature, exceptions);
	}
}
