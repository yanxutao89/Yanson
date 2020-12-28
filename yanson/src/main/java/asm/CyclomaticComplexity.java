package asm;

import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/28 8:07
 */
public class CyclomaticComplexity {
    public int getCyclomaticComplexity(String owner, MethodNode mn) throws AnalyzerException {
        Analyzer<BasicValue> a = new Analyzer<BasicValue>(new BasicInterpreter()) {
                    protected Frame<BasicValue> newFrame(int nLocals, int nStack) {
                        return new Node<BasicValue>(nLocals, nStack);
                    }
                    protected Frame<BasicValue> newFrame(
                            Frame<? extends BasicValue> src) {
                        return new Node<BasicValue>(src);
                    }
                    protected void newControlFlowEdge(int src, int dst) {
                        Node<BasicValue> s = (Node<BasicValue>) getFrames()[src];
                        s.successors.add((Node<BasicValue>) getFrames()[dst]);
                    }
                };
        a.analyze(owner, mn);
        Frame<BasicValue>[] frames = a.getFrames();
        int edges = 0;
        int nodes = 0;
        for (int i = 0; i < frames.length; ++i) {
            if (frames[i] != null) {
                edges += ((Node<BasicValue>) frames[i]).successors.size();
                nodes += 1;
            }
        }
        return edges - nodes + 2;
    }
}

class Node<V extends Value> extends Frame<V> {
    Set<Node<V>> successors = new HashSet<Node<V>>();

    public Node(int nLocals, int nStack) {
        super(nLocals, nStack);
    }

    public Node(Frame<? extends V> src) {
        super(src);
    }
}
