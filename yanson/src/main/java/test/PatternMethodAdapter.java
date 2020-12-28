package test;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/26 16:52
 */
public abstract class PatternMethodAdapter extends MethodVisitor {
	protected final static int SEEN_NOTHING = 0;
	protected int state;

	public PatternMethodAdapter(int api, MethodVisitor mv) {
		super(api, mv);
	}

	@Override
	public void visitInsn(int opcode) {
		visitInsn();
		mv.visitInsn(opcode);
	}

	@Override
	public void visitIntInsn(int opcode, int operand) {
		visitInsn();
		mv.visitIntInsn(opcode, operand);
	}

	@Override
	public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
		visitInsn();
		mv.visitFrame(type, nLocal, local, nStack, stack);
	}

	@Override
	public void visitLabel(Label label) {
		visitInsn();
		mv.visitLabel(label);
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		visitInsn();
		mv.visitMaxs(maxStack, maxLocals);
	}

	protected abstract void visitInsn();
}
