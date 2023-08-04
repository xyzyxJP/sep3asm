package lang.sep3asm.parse;

import lang.FatalErrorException;
import lang.sep3asm.Sep3asmParseContext;
import lang.sep3asm.Sep3asmParseRule;
import lang.sep3asm.Sep3asmToken;
import lang.sep3asm.Sep3asmTokenizer;

public class PseudoInstLine extends Sep3asmParseRule {
    // pseudoInstLine ::= wordAlloc | blockAlloc | startAddr | end

    private Sep3asmParseRule syn;

    public PseudoInstLine(Sep3asmParseContext ctx) {
    }

    public static boolean isFirst(Sep3asmToken tk) {
        return WordAlloc.isFirst(tk) || BlockAlloc.isFirst(tk) || StartAddr.isFirst(tk)
                || tk.getType() == Sep3asmToken.TK_DOT_END;
    }

    public void parse(Sep3asmParseContext ctx) throws FatalErrorException {
        Sep3asmTokenizer ct = ctx.getTokenizer();
        Sep3asmToken tk = ct.getCurrentToken(ctx);
        if (WordAlloc.isFirst(tk)) {
            syn = new WordAlloc();
        } else if (BlockAlloc.isFirst(tk)) {
            syn = new BlockAlloc();
        } else if (StartAddr.isFirst(tk)) {
            syn = new StartAddr();
        } else if (tk.getType() == Sep3asmToken.TK_DOT_END) {
            ct.getNextToken(ctx);
            ctx.hasDotEnd = true;
        }
        if (syn != null) {
            syn.parse(ctx);
        }
    }

    public void pass1(Sep3asmParseContext ctx) throws FatalErrorException {
        if (syn != null) {
            syn.pass1(ctx);
        }
    }

    public void pass2(Sep3asmParseContext pcx) throws FatalErrorException {
        if (syn != null) {
            syn.pass2(pcx);
        }
    }
}
