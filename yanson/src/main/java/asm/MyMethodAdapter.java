package asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;

import static org.objectweb.asm.Opcodes.ASM4;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 7:31
 */
public class MyMethodAdapter extends MethodNode {
    public MyMethodAdapter(int access, String name, String desc,
                           String signature, String[] exceptions, MethodVisitor mv) {
        super(ASM4, access, name, desc, signature, exceptions);
        this.mv = mv;
    }

    @Override
    public void visitEnd() {
        // put your transformation code here
        accept(mv);
    }
}
