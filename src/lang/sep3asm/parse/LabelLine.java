package lang.sep3asm.parse;

import lang.FatalErrorException;
import lang.sep3asm.LabelEntry;
import lang.sep3asm.Sep3asmParseContext;
import lang.sep3asm.Sep3asmParseRule;
import lang.sep3asm.Sep3asmToken;
import lang.sep3asm.Sep3asmTokenizer;

public class LabelLine extends Sep3asmParseRule {
    // labelLine ::= IDENT (COLON | EQUAL (NUM | TEXT))

    private Sep3asmToken label;
    private Sep3asmToken rhs;

    public LabelLine() {
    }

    public static boolean isFirst(Sep3asmToken tk) {
        return tk.getType() == Sep3asmToken.TK_IDENT;
    }

    public void parse(Sep3asmParseContext ctx) throws FatalErrorException {
        Sep3asmTokenizer ct = ctx.getTokenizer();
        label = ct.getCurrentToken(ctx);
        Sep3asmToken tk = ct.getNextToken(ctx);
        switch (tk.getType()) {
            case Sep3asmToken.TK_COLON:
                ct.getNextToken(ctx);
                break;
            case Sep3asmToken.TK_EQUAL:
                rhs = ct.getNextToken(ctx);
                ct.getNextToken(ctx);
                break;
            default:
                ctx.warning("':' or '=' is expected after " + tk.toExplainString());
                break;
        }
    }

    public void pass1(Sep3asmParseContext ctx) throws FatalErrorException {
        LabelEntry le = new LabelEntry();
        if (rhs == null) {
            le.setInteger(ctx.getLocationCounter());
        } else if (rhs.getType() == Sep3asmToken.TK_NUM) {
            le.setInteger(rhs.getIntValue());
        } else {
            le.setLabel(rhs.getText());
        }
        ctx.getSymbolTable().register(label.getText(), le);
    }

    public void pass2(Sep3asmParseContext ctx) throws FatalErrorException {
    }
}
