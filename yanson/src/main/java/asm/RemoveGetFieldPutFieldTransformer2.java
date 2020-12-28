package asm;

import org.objectweb.asm.tree.*;

import java.util.Iterator;

import static org.objectweb.asm.Opcodes.*;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 7:23
 */
public class RemoveGetFieldPutFieldTransformer2 extends MethodTransformer {

    public RemoveGetFieldPutFieldTransformer2(MethodTransformer mt) {
        super(mt);
    }

    @Override
    public void transform(MethodNode mn) {
        InsnList insns = mn.instructions;
        Iterator i = insns.iterator();
        while (i.hasNext()) {
            AbstractInsnNode i1 = (AbstractInsnNode) i.next();
            if (isALOAD0(i1)) {
                AbstractInsnNode i2 = getNext(i);
                if (i2 != null && isALOAD0(i2)) {
                    AbstractInsnNode i3 = getNext(i);
                    while (i3 != null && isALOAD0(i3)) {
                        i1 = i2;
                        i2 = i3;
                        i3 = getNext(i);
                    }
                    if (i3 != null && i3.getOpcode() == GETFIELD) {
                        AbstractInsnNode i4 = getNext(i);
                        if (i4 != null && i4.getOpcode() == PUTFIELD) {
                            if (sameField(i3, i4)) {
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
    }

    private static boolean isALOAD0(AbstractInsnNode i) {
        return i.getOpcode() == ALOAD && ((VarInsnNode) i).var == 0;
    }

    private static boolean sameField(AbstractInsnNode i, AbstractInsnNode j) {
        return ((FieldInsnNode) i).name.equals(((FieldInsnNode) j).name);
    }

    private static AbstractInsnNode getNext(Iterator i) {
        while (i.hasNext()) {
            AbstractInsnNode in = (AbstractInsnNode) i.next();
            if (!(in instanceof LineNumberNode)) {
                return in;
            }
        }
        return null;
    }
}
