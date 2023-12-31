package lang.sep3asm.parse;

import lang.FatalErrorException;
import lang.sep3asm.Sep3asmParseContext;
import lang.sep3asm.Sep3asmParseRule;
import lang.sep3asm.Sep3asmToken;
import lang.sep3asm.Sep3asmTokenizer;

public class Line extends Sep3asmParseRule {
    // lineLine ::= (labelLine | instLine | comment) NL

    private Sep3asmParseRule syn;

    public Line(Sep3asmParseContext ctx) {
    }

    public static boolean isFirst(Sep3asmToken tk) {
        return LabelLine.isFirst(tk) || InstLine.isFirst(tk) || Comment.isFirst(tk)
                || tk.getType() == Sep3asmToken.TK_NL;
    }

    public void parse(Sep3asmParseContext ctx) throws FatalErrorException {
        Sep3asmTokenizer ct = ctx.getTokenizer();
        Sep3asmToken tk = ct.getCurrentToken(ctx);
        if (LabelLine.isFirst(tk)) {
            syn = new LabelLine();
        } else if (InstLine.isFirst(tk)) {
            syn = new InstLine();
        } else if (Comment.isFirst(tk)) {
            syn = new Comment();
        }
        if (syn != null) {
            syn.parse(ctx);
        }
        tk = ct.getCurrentToken(ctx);
        if (tk.getType() != Sep3asmToken.TK_NL) {
            if (tk.getType() != Sep3asmToken.TK_SEMI_COLON && !(syn instanceof Comment)) {
                ctx.warning("There is garbage at the end of the file: " + tk.toExplainString());
            }
            ct.skipToNL(ctx);
        }
        ct.getNextToken(ctx);
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
