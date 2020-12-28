package test;

import org.objectweb.asm.tree.*;

import java.util.Iterator;

import static org.objectweb.asm.Opcodes.*;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 7:17
 */
public class RemoveGetFieldPutFieldTransformer extends MethodTransformer {
    public RemoveGetFieldPutFieldTransformer(MethodTransformer mt) {
        super(mt);
    }

    @Override
    public MethodNode transform(MethodNode mn) {
        InsnList insns = mn.instructions;
        Iterator<AbstractInsnNode> i = insns.iterator();
        while (i.hasNext()) {
            AbstractInsnNode i1 = i.next();
            if (isALOAD0(i1)) {
                AbstractInsnNode i2 = getNext(i1);
                if (i2 != null && isALOAD0(i2)) {
                    AbstractInsnNode i3 = getNext(i2);
                    if (i3 != null && i3.getOpcode() == GETFIELD) {
                        AbstractInsnNode i4 = getNext(i3);
                        if (i4 != null && i4.getOpcode() == PUTFIELD) {
                            if (sameField(i3, i4)) {
                                while (i.next() != i4) {
                                }
                                insns.remove(i1);
                                insns.remove(i2);
                                insns.remove(i3);
                                insns.remove(i4);
                            }
                        }
                    }
                }
            }
        }
        super.transform(mn);
        return mn;
    }

    private static AbstractInsnNode getNext(AbstractInsnNode insn) {
        do {
            insn = insn.getNext();
            if (insn != null && !(insn instanceof LineNumberNode)) {
                break;
            }
        } while (insn != null);
        return insn;
    }

    private static boolean isALOAD0(AbstractInsnNode i) {
        return i.getOpcode() == ALOAD && ((VarInsnNode) i).var == 0;
    }

    private static boolean sameField(AbstractInsnNode i, AbstractInsnNode j) {
        return ((FieldInsnNode) i).name.equals(((FieldInsnNode) j).name);
    }
}
