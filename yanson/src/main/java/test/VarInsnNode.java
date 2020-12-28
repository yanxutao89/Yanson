package test;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;

import java.util.Map;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 7:07
 */
public class VarInsnNode extends AbstractInsnNode {
    public int var;

    public VarInsnNode(int opcode, int var) {
        super(opcode);
        this.var = var;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public void accept(MethodVisitor methodVisitor) {

    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> map) {
        return null;
    }
}
