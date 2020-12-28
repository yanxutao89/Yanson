package test;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Value;

import static org.objectweb.asm.tree.analysis.BasicValue.REFERENCE_VALUE;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 7:55
 */
class IsNullInterpreter extends BasicInterpreter {
    public final static BasicValue NULL = new BasicValue(null);
    public final static BasicValue MAYBENULL = new BasicValue(null);

    public IsNullInterpreter() {
        super(ASM4);
    }

    @Override
    public BasicValue newOperation(AbstractInsnNode insn) throws AnalyzerException {
        if (insn.getOpcode() == ACONST_NULL) {
            return NULL;
        }
        return super.newOperation(insn);
    }

    @Override
    public BasicValue merge(BasicValue v, BasicValue w) {
        if (isRef(v) && isRef(w) && v != w) {
            return MAYBENULL;
        }
        return super.merge(v, w);
    }

    private boolean isRef(Value v) {
        return v == REFERENCE_VALUE || v == NULL || v == MAYBENULL;
    }
}
