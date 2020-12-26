package asm;

import org.objectweb.asm.signature.SignatureVisitor;

import java.util.Map;

import static org.objectweb.asm.Opcodes.ASM4;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/26 18:33
 */
public class RenameSignatureAdapter extends SignatureVisitor {
	private SignatureVisitor sv;
	private Map<String, String> renaming;
	private String oldName;

	public RenameSignatureAdapter(SignatureVisitor sv, Map<String, String> renaming) {
		super(ASM4);
		this.sv = sv;
		this.renaming = renaming;
	}

	public void visitFormalTypeParameter(String name) {
		sv.visitFormalTypeParameter(name);
	}

	public SignatureVisitor visitClassBound() {
		sv.visitClassBound();
		return this;
	}

	public SignatureVisitor visitInterfaceBound() {
		sv.visitInterfaceBound();
		return this;
	}

	public void visitClassType(String name) {
		oldName = name;
		String newName = renaming.get(oldName);
		sv.visitClassType(newName == null ? name : newName);
	}

	public void visitInnerClassType(String name) {
		oldName = oldName + "." + name;
		String newName = renaming.get(oldName);
		sv.visitInnerClassType(newName == null ? name : newName);
	}

	public void visitTypeArgument() {
		sv.visitTypeArgument();
	}

	public SignatureVisitor visitTypeArgument(char wildcard) {
		sv.visitTypeArgument(wildcard);
		return this;
	}

	public void visitEnd() {
		sv.visitEnd();
	}
}