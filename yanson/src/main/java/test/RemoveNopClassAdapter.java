package test;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM4;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/26 16:14
 */
public class RemoveNopClassAdapter extends ClassVisitor {
	public RemoveNopClassAdapter(ClassVisitor cv) {
		super(ASM4, cv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv;
		mv = cv.visitMethod(access, name, desc, signature, exceptions);
		if (mv != null && !"<init>".equals(name)) {
			mv = new RemoveNopAdapter(mv);
		}
		return mv;
	}
}
