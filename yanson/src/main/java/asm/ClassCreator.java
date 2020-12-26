package asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import static org.objectweb.asm.Opcodes.*;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/26 13:27
 */
public class ClassCreator {

	public static byte[] createClassWithByteArray() throws Exception {
		ClassWriter cw = new ClassWriter(0);
		AddTimerAdapter ata = new AddTimerAdapter(cw);
		PrintWriter printWriter = new PrintWriter(new FileOutputStream("Comparable"));
		TraceClassVisitor tcv = new TraceClassVisitor(ata, printWriter);
		CheckClassAdapter cca = new CheckClassAdapter(tcv);
		tcv.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
				"pkg/Comparable", null, "java/lang/Object",
				new String[]{"pkg/Measurable"});
		tcv.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "LESS", "I",
				null, new Integer(-1)).visitEnd();
		tcv.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL", "I",
				null, new Integer(0)).visitEnd();
		tcv.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GREATER", "I",
				null, new Integer(1)).visitEnd();
		tcv.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "compareTo",
				"(Ljava/lang/Object;)I", null, null).visitEnd();
		tcv.visitEnd();

		System.out.println(tcv.p.getText());

		byte[] b = cw.toByteArray();

		return b;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(Arrays.toString(createClassWithByteArray()));
	}
}
