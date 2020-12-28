package asm;

import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 7:14
 */
public class AddTimerTransformer extends ClassTransformer {
    public AddTimerTransformer(ClassTransformer ct) {
        super(ct);
    }

    @Override
    public void transform(ClassNode cn) {
        for (MethodNode mn : (List<MethodNode>) cn.methods) {
            if ("<init>".equals(mn.name) || "<clinit>".equals(mn.name)) {
                continue;
            }
            InsnList insns = mn.instructions;
            if (insns.size() == 0) {
                continue;
            }
            Iterator<AbstractInsnNode> j = insns.iterator();
            while (j.hasNext()) {
                AbstractInsnNode in = j.next();
                int op = in.getOpcode();
                if ((op >= IRETURN && op <= RETURN) || op == ATHROW) {
                    InsnList il = new InsnList();
                    il.add(new FieldInsnNode(GETSTATIC, cn.name, "timer", "J"));
                    il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/System",
                            "currentTimeMillis", "()J"));
                    il.add(new InsnNode(LADD));
                    il.add(new FieldInsnNode(PUTSTATIC, cn.name, "timer", "J"));
                    insns.insert(in.getPrevious(), il);
                }
            }
            InsnList il = new InsnList();
            il.add(new FieldInsnNode(GETSTATIC, cn.name, "timer", "J"));
            il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/System",
                    "currentTimeMillis", "()J"));
            il.add(new InsnNode(LSUB));
            il.add(new FieldInsnNode(PUTSTATIC, cn.name, "timer", "J"));
            insns.insert(il);
            mn.maxStack += 4;
        }
        int acc = ACC_PUBLIC + ACC_STATIC;
        cn.fields.add(new FieldNode(acc, "timer", "J", null, null));
        super.transform(cn);
    }
}
