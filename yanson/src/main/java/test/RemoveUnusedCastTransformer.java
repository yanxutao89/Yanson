package test;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.analysis.*;

import static org.objectweb.asm.Opcodes.CHECKCAST;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 7:48
 */
public class RemoveUnusedCastTransformer extends MethodTransformer {
    String owner;

    public RemoveUnusedCastTransformer(String owner, MethodTransformer mt) {
        super(mt);
        this.owner = owner;
    }

    @Override
    public MethodNode transform(MethodNode mn) {
        Analyzer<BasicValue> a = new Analyzer<BasicValue>(new SimpleVerifier());
        try {
            a.analyze(owner, mn);
            Frame<BasicValue>[] frames = a.getFrames();
            AbstractInsnNode[] insns = mn.instructions.toArray();
            for (int i = 0; i < insns.length; ++i) {
                AbstractInsnNode insn = insns[i];
                if (insn.getOpcode() == CHECKCAST) {
                    Frame f = frames[i];
                    if (f != null && f.getStackSize() > 0) {
                        Object operand = f.getStack(f.getStackSize() - 1);
                        Class<?> to = getClass(((TypeInsnNode) insn).desc);
                        Class<?> from = getClass(((BasicValue) operand).getType());
                        if (to.isAssignableFrom(from)) {
                            mn.instructions.remove(insn);
                        }
                    }
                }
            }
        } catch (AnalyzerException ignored) {
        }
        return mt == null ? mn : mt.transform(mn);
    }

    private static Class<?> getClass(String desc) {
        try {
            return Class.forName(desc.replace('/', '.'));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.toString());
        }
    }

    private static Class<?> getClass(Type t) {
        if (t.getSort() == Type.OBJECT) {
            return getClass(t.getInternalName());
        }
        return getClass(t.getDescriptor());
    }
}
