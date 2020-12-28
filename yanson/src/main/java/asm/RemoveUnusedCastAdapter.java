package asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AnalyzerAdapter;

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.CHECKCAST;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 7:52
 */
public class RemoveUnusedCastAdapter extends MethodVisitor {
    public AnalyzerAdapter aa;

    public RemoveUnusedCastAdapter(MethodVisitor mv) {
        super(ASM4, mv);
    }

    @Override
    public void visitTypeInsn(int opcode, String desc) {
        if (opcode == CHECKCAST) {
            Class<?> to = getClass(desc);
            if (aa.stack != null && aa.stack.size() > 0) {
                Object operand = aa.stack.get(aa.stack.size() - 1);
                if (operand instanceof String) {
                    Class<?> from = getClass((String) operand);
                    if (to.isAssignableFrom(from)) {
                        return;
                    }
                }
            }
        }
        mv.visitTypeInsn(opcode, desc);
    }

    private static Class getClass(String desc) {
        try {
            return Class.forName(desc.replace('/', '.'));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.toString());
        }
    }
}
