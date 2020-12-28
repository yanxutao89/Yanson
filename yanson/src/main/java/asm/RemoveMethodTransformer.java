package asm;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 6:52
 */
public class RemoveMethodTransformer extends ClassTransformer {
    private String methodName;
    private String methodDesc;

    public RemoveMethodTransformer(ClassTransformer ct, String methodName, String methodDesc) {
        super(ct);
        this.methodName = methodName;
        this.methodDesc = methodDesc;
    }

    @Override
    public void transform(ClassNode cn) {
        Iterator<MethodNode> i = cn.methods.iterator();
        while (i.hasNext()) {
            MethodNode mn = i.next();
            if (methodName.equals(mn.name) && methodDesc.equals(mn.desc)) {
                i.remove();
            }
        }
        super.transform(cn);
    }
}
