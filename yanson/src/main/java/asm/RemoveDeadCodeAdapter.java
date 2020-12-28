package asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.*;

import static org.objectweb.asm.Opcodes.ASM4;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 7:39
 */
public class RemoveDeadCodeAdapter extends MethodVisitor {
    String owner;
    MethodVisitor next;

    public RemoveDeadCodeAdapter(String owner, int access, String name, String desc, MethodVisitor mv) {
        super(ASM4, new MethodNode(access, name, desc, null, null));
        this.owner = owner;
        next = mv;
    }

    @Override
    public void visitEnd() {
        MethodNode mn = (MethodNode) mv;
        Analyzer<BasicValue> a = new Analyzer<BasicValue>(new BasicInterpreter());
        try {
            a.analyze(owner, mn);
            Frame<BasicValue>[] frames = a.getFrames();
            AbstractInsnNode[] insns = mn.instructions.toArray();
            for (int i = 0; i < frames.length; ++i) {
                if (frames[i] == null && !(insns[i] instanceof LabelNode)) {
                    mn.instructions.remove(insns[i]);
                }
            }
        } catch (AnalyzerException ignored) {
        }
        mn.accept(next);
    }
}
