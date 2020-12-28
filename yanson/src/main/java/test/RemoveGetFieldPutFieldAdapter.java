package test;

import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/26 17:01
 */
class RemoveGetFieldPutFieldAdapter extends PatternMethodAdapter {
	private final static int SEEN_ALOAD_0 = 1;
	private final static int SEEN_ALOAD_0ALOAD_0 = 2;
	private final static int SEEN_ALOAD_0ALOAD_0GETFIELD = 3;
	private String fieldOwner;
	private String fieldName;
	private String fieldDesc;

	public RemoveGetFieldPutFieldAdapter(int api, MethodVisitor mv) {
		super(api, mv);
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		switch (state) {
			case SEEN_NOTHING: // S0 -> S1
				if (opcode == ALOAD && var == 0) {
					state = SEEN_ALOAD_0;
					return;
				}
				break;
			case SEEN_ALOAD_0: // S1 -> S2
				if (opcode == ALOAD && var == 0) {
					state = SEEN_ALOAD_0ALOAD_0;
					return;
				}
				break;
			case SEEN_ALOAD_0ALOAD_0: // S2 -> S2
				if (opcode == ALOAD && var == 0) {
					mv.visitVarInsn(ALOAD, 0);
					return;
				}
				break;
		}
		visitInsn();
		mv.visitVarInsn(opcode, var);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		switch (state) {
			case SEEN_ALOAD_0ALOAD_0: // S2 -> S3
				if (opcode == GETFIELD) {
					state = SEEN_ALOAD_0ALOAD_0GETFIELD;
					fieldOwner = owner;
					fieldName = name;
					fieldDesc = desc;
					return;
				}
				break;
			case SEEN_ALOAD_0ALOAD_0GETFIELD: // S3 -> S0
				if (opcode == PUTFIELD && name.equals(fieldName)) {
					state = SEEN_NOTHING;
					return;
				}
				break;
		}
		visitInsn();
		mv.visitFieldInsn(opcode, owner, name, desc);
	}

	@Override
	protected void visitInsn() {
		switch (state) {
			case SEEN_ALOAD_0: // S1 -> S0
				mv.visitVarInsn(ALOAD, 0);
				break;
			case SEEN_ALOAD_0ALOAD_0: // S2 -> S0
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 0);
				break;
			case SEEN_ALOAD_0ALOAD_0GETFIELD: // S3 -> S0
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, fieldOwner, fieldName, fieldDesc);
				break;
		}
		state = SEEN_NOTHING;
	}
}
