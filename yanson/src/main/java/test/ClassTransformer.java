package test;

import org.objectweb.asm.tree.ClassNode;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 6:51
 */
public class ClassTransformer {
    protected ClassTransformer ct;

    public ClassTransformer(ClassTransformer ct) {
        this.ct = ct;
    }

    public void transform(ClassNode cn) {
        if (ct != null) {
            ct.transform(cn);
        }
    }
}
