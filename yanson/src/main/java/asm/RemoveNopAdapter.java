package asm;

import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.NOP;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/26 16:12
 */
public class RemoveNopAdapter extends MethodVisitor {
	public RemoveNopAdapter(MethodVisitor mv) {
		super(ASM4, mv);
	}

	@Override
	public void visitInsn(int opcode) {
		if (opcode != NOP) {
			mv.visitInsn(opcode);
		}
	}
}
