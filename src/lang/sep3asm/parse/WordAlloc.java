package lang.sep3asm.parse;

import java.util.ArrayList;

import lang.FatalErrorException;
import lang.sep3asm.LabelEntry;
import lang.sep3asm.Sep3asmParseContext;
import lang.sep3asm.Sep3asmParseRule;
import lang.sep3asm.Sep3asmToken;
import lang.sep3asm.Sep3asmTokenizer;

public class WordAlloc extends Sep3asmParseRule {
    private ArrayList<Sep3asmToken> tks = new ArrayList<Sep3asmToken>();

    public static boolean isFirst(Sep3asmToken tk) {
        return tk.getType() == Sep3asmToken.TK_DOT_WORD;
    }

    public void parse(Sep3asmParseContext ctx) throws FatalErrorException {
        Sep3asmTokenizer ct = ctx.getTokenizer();
        Sep3asmToken tk = ct.getCurrentToken(ctx);
        while (tk.getType() != Sep3asmToken.TK_NL || tk.getType() != Sep3asmToken.TK_EOF) {
            tk = ct.getNextToken(ctx);
            switch (tk.getType()) {
                case Sep3asmToken.TK_COMMA:
                    break;
                case Sep3asmToken.TK_NUM:
                case Sep3asmToken.TK_IDENT:
                    tks.add(tk);
                    break;
                case Sep3asmToken.TK_NL:
                case Sep3asmToken.TK_EOF:
                    return;
                default:
                    ctx.warning("',' or NL or EOF is expected after " + tk.toExplainString());
            }
        }
    }

    public void pass1(Sep3asmParseContext ctx) throws FatalErrorException {
        for (Sep3asmToken tk : tks) {
            if (tk.getType() == Sep3asmToken.TK_IDENT) {
                ctx.getSymbolTable().register(tk.getText(), null);
            }
            ctx.addLocationCounter(1);
        }
    }

    public void pass2(Sep3asmParseContext ctx) throws FatalErrorException {
        for (Sep3asmToken tk : tks) {
            // TODO:
            // int data;
            if (tk.getType() == Sep3asmToken.TK_IDENT) {
                LabelEntry le = ctx.getSymbolTable().search(tk.getText());
                if (le == null || !le.isLabel()) {
                    ctx.error("undefined label: " + tk.toExplainString());
                    return;
                }
                // data = le.getInteger();
            } else {
                // data = tk.getIntValue();
            }
            ctx.addLocationCounter(1);
        }
    }
}