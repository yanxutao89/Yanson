package test;

import org.objectweb.asm.ClassVisitor;

import static org.objectweb.asm.Opcodes.ASM4;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/26 13:53
 */
public class RemoveDebugAdapter extends ClassVisitor {
	public RemoveDebugAdapter(ClassVisitor cv) {
		super(ASM4, cv);
	}

	@Override
	public void visitSource(String source, String debug) {
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {
	}

	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
	}
}
