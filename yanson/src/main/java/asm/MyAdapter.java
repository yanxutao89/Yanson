package asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM4;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/26 23:10
 */
public class MyAdapter extends MethodVisitor {
    int currentLine;

    public MyAdapter(MethodVisitor mv) {
        super(ASM4, mv);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        mv.visitLineNumber(line, start);
        currentLine = line;
    }
}
