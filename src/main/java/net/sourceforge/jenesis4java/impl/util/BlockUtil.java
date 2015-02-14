package net.sourceforge.jenesis4java.impl.util;

import java.util.List;

import net.sourceforge.jenesis4java.ElseIf;
import net.sourceforge.jenesis4java.If;
import net.sourceforge.jenesis4java.LocalBlock;
import net.sourceforge.jenesis4java.Statement;

public class BlockUtil {

    public static boolean isBlockWithAbruptCompletion(List<Statement> stmts) {
        Statement last;
        if (stmts.size() > 0) {
            last = stmts.get(stmts.size() - 1);
        } else {
            return false;
        }

        if (last instanceof LocalBlock) {
            return last.isBlockWithAbruptCompletion();

        }
        if (last instanceof If) {
            If ifStmt = (If) last;
            List<Statement> subStatements = ifStmt.getStatements();
            boolean result = isBlockWithAbruptCompletion(subStatements);
            if (result) {
                if (ifStmt.getElse() != null) {
                    result = isBlockWithAbruptCompletion(ifStmt.getElse().getStatements());
                }
            }
            if (ifStmt.getElseIfs() != null) {
                for (ElseIf elseif : ifStmt.getElseIfs()) {
                    if (result) {
                        result = isBlockWithAbruptCompletion(elseif.getStatements());
                    }
                }
            }
            return result;
        }
        return last.isAbruptCompletionStatement();
    }
}
