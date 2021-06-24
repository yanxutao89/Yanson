package yanson.asm;


import org.objectweb.asm.ClassVisitor;

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.V1_8;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 14:36
 */
public class VersionAdapter extends ClassVisitor {
	public VersionAdapter() {
		super(ASM4);
	}

	public VersionAdapter(ClassVisitor classVisitor) {
		super(ASM4, classVisitor);
	}

	@Override
	public void visit(int i, int i1, String s, String s1, String s2, String[] strings) {
		cv.visit(V1_8, i1, s, s1, s2, strings);
	}
}
