package asm;

import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.ASM4;


/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 11:42
 */
public class ClassPrinter extends ClassVisitor {

	public ClassPrinter() {
		super(ASM4);
	}

	public ClassPrinter(ClassVisitor classVisitor) {
		super(ASM4, classVisitor);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		System.out.println("visit");
	}

	@Override
	public void visitSource(String source, String debug) {
		System.out.println("visitSource");
	}

	@Override
	public ModuleVisitor visitModule(String name, int access, String version) {
		System.out.println("visitModule");
		return super.visitModule(name, access, version);
	}

	@Override
	public void visitNestHost(String nestHost) {
		System.out.println("visitNestHost");
	}

	@Override
	public void visitOuterClass(String owner, String name, String descriptor) {
		System.out.println("visitOuterClass");
	}

	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
		System.out.println("visitAnnotation");
		return super.visitAnnotation(descriptor, visible);
	}

	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
		System.out.println("visitTypeAnnotation");
		return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
	}

	@Override
	public void visitAttribute(Attribute attribute) {
		System.out.println("visitAttribute");
	}

	@Override
	public void visitNestMember(String nestMember) {
		System.out.println("visitNestMember");
	}

	@Override
	public void visitPermittedSubclass(String permittedSubclass) {
		System.out.println("visitPermittedSubClass");
	}

	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
		System.out.println("visitInnerClass");
	}

	@Override
	public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
		System.out.println("visitRecordComponent");
		return super.visitRecordComponent(name, descriptor, signature);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		System.out.println("visitField");
		return super.visitField(access, name, descriptor, signature, value);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
		System.out.println("visitMethod");
		return super.visitMethod(access, name, descriptor, signature, exceptions);
	}

	@Override
	public void visitEnd() {
		System.out.println("visitEnd");
	}
}
