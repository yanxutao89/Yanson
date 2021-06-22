package yanson.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ASM4;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 18:40
 */
public class AddFieldAdapter extends ClassVisitor {

	private int fieldAcc;
	private String fieldName;
	private String fieldDesc;
	private boolean isFieldPresent;

	public AddFieldAdapter(int fieldAcc, String fieldName, String fieldDesc) {
		super(ASM4);
		this.fieldAcc = fieldAcc;
		this.fieldName = fieldName;
		this.fieldDesc = fieldDesc;
	}

	public AddFieldAdapter(ClassVisitor classVisitor, int fieldAcc, String fieldName, String fieldDesc) {
		super(ASM4, classVisitor);
		this.fieldAcc = fieldAcc;
		this.fieldName = fieldName;
		this.fieldDesc = fieldDesc;
	}

	@Override
	public FieldVisitor visitField(int i, String s, String s1, String s2, Object o) {
		if (s.equals(fieldName) && s1.equals(fieldDesc)) {
			isFieldPresent = true;
		}
		return cv.visitField(i, s, s1, s2, o);
	}

	@Override
	public void visitEnd() {
		if (!isFieldPresent) {
			FieldVisitor fieldVisitor = cv.visitField(fieldAcc, fieldName, fieldDesc, null, null);
			if (null != fieldVisitor) {
				fieldVisitor.visitEnd();
			}
		}
		cv.visitEnd();
	}

	public static void main(String[] args) throws IOException {
		String name = "";
		ClassReader classReader = new ClassReader(name);
		ClassWriter classWriter = new ClassWriter(classReader, 0);
		AddFieldAdapter addFieldAdapter = new AddFieldAdapter(classWriter, ACC_PRIVATE, "count", "J");
		TraceClassVisitor traceClassVisitor = new TraceClassVisitor(addFieldAdapter, new Textifier(), new PrintWriter(new FileOutputStream(name)));
		CheckClassAdapter checkClassAdapter = new CheckClassAdapter(traceClassVisitor);
		classReader.accept(checkClassAdapter, 0);
		byte[] bytes = classWriter.toByteArray();
		MyClassLoader myClassLoader = new MyClassLoader();
		Class clazz = myClassLoader.defineClass(null, bytes);
		System.out.println(Arrays.toString(clazz.getDeclaredFields()));

	}

}
