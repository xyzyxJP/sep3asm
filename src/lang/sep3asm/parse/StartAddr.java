package lang.sep3asm.parse;

import lang.FatalErrorException;
import lang.sep3asm.LabelEntry;
import lang.sep3asm.Sep3asmParseContext;
import lang.sep3asm.Sep3asmParseRule;
import lang.sep3asm.Sep3asmSymbolTable;
import lang.sep3asm.Sep3asmToken;
import lang.sep3asm.Sep3asmTokenizer;

public class StartAddr extends Sep3asmParseRule {
    private Sep3asmToken rhs;

    public static boolean isFirst(Sep3asmToken tk) {
        return tk.getType() == Sep3asmToken.TK_DOT;
    }

    public void parse(Sep3asmParseContext ctx) throws FatalErrorException {
        Sep3asmTokenizer ct = ctx.getTokenizer();
        Sep3asmToken tk = ct.getNextToken(ctx);
        if (tk.getType() != Sep3asmToken.TK_EQUAL) {
            ctx.warning("= is expected after " + tk.toExplainString());
        }
        tk = ct.getNextToken(ctx);
        switch (tk.getType()) {
            case Sep3asmToken.TK_NUM:
            case Sep3asmToken.TK_IDENT:
                rhs = tk;
                break;
            default:
                ctx.warning("number or label is expected after " + tk.toExplainString());
                break;
        }
        ct.getNextToken(ctx);
    }

    public void pass1(Sep3asmParseContext ctx) throws FatalErrorException {
        if (rhs.getType() == Sep3asmToken.TK_NUM) {
            ctx.setLocationCounter(rhs.getIntValue());
        } else {
            Sep3asmSymbolTable st = ctx.getSymbolTable();
            LabelEntry le = st.search(rhs.getText());
            if (le == null || !le.isLabel()) {
                ctx.error("undefined label: " + rhs.toExplainString());
                return;
            }
            ctx.setLocationCounter(le.getInteger());
        }
    }

    public void pass2(Sep3asmParseContext ctx) throws FatalErrorException {
        pass1(ctx);
    }
}