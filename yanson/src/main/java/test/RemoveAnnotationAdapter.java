package test;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;

import static org.objectweb.asm.Opcodes.ASM4;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/26 22:33
 */
public class RemoveAnnotationAdapter extends ClassVisitor {
    private String annDesc;

    public RemoveAnnotationAdapter(ClassVisitor cv, String annDesc) {
        super(ASM4, cv);
        this.annDesc = annDesc;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean vis) {
        if (desc.equals(annDesc)) {
            return null;
        }
        return cv.visitAnnotation(desc, vis);
    }
}
