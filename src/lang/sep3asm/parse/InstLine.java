package lang.sep3asm.parse;

import lang.FatalErrorException;
import lang.sep3asm.Sep3asmParseContext;
import lang.sep3asm.Sep3asmParseRule;
import lang.sep3asm.Sep3asmToken;
import lang.sep3asm.Sep3asmTokenizer;

public class InstLine extends Sep3asmParseRule {
    // instLine ::= inst0 | inst1a | inst1b | inst2

    private Sep3asmParseRule syn;

    public InstLine() {
    }

    public static boolean isFirst(Sep3asmToken tk) {
        return Inst0.isFirst(tk) || Inst1A.isFirst(tk) || Inst1B.isFirst(tk) || Inst2.isFirst(tk);
    }

    public void parse(Sep3asmParseContext ctx) throws FatalErrorException {
        Sep3asmTokenizer ct = ctx.getTokenizer();
        Sep3asmToken tk = ct.getCurrentToken(ctx);
        if (Inst0.isFirst(tk)) {
            syn = new Inst0();
        } else if (Inst1A.isFirst(tk)) {
            syn = new Inst1A(ctx);
        } else if (Inst1B.isFirst(tk)) {
            syn = new Inst1B(ctx);
        } else if (Inst2.isFirst(tk)) {
            syn = new Inst2(ctx);
        } else {
            ctx.warning("instruction is expected after " + tk.toExplainString());
        }
        syn.parse(ctx);
    }

    public void pass1(Sep3asmParseContext ctx) throws FatalErrorException {
        syn.pass1(ctx);
    }

    public void pass2(Sep3asmParseContext ctx) throws FatalErrorException {
        syn.pass2(ctx);
    }
}
