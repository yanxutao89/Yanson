package test;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/26 22:39
 */
public class DeprecatedDump implements Opcodes {
    public static byte[] dump() throws Exception {
        ClassWriter cw = new ClassWriter(0);
        AnnotationVisitor av;
        cw.visit(V1_5, ACC_PUBLIC + ACC_ANNOTATION + ACC_ABSTRACT
                        + ACC_INTERFACE, "java/lang/Deprecated", null,
                "java/lang/Object",
                new String[]{"java/lang/annotation/Annotation"});
        {
            av = cw.visitAnnotation("Ljava/lang/annotation/Documented;",
                    true);
            av.visitEnd();
        }
        {
            av = cw.visitAnnotation("Ljava/lang/annotation/Retention;", true);
            av.visitEnum("value", "Ljava/lang/annotation/RetentionPolicy;",
                    "RUNTIME");
            av.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }
}
