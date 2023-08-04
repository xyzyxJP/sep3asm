package lang.sep3asm.parse;

import lang.FatalErrorException;
import lang.sep3asm.LabelEntry;
import lang.sep3asm.Sep3asmParseContext;
import lang.sep3asm.Sep3asmParseRule;
import lang.sep3asm.Sep3asmToken;
import lang.sep3asm.Sep3asmTokenizer;

public class Operand extends Sep3asmParseRule {
    // operand ::= REG | PAR_L REG RAR_R | PAR_L REG RAR_R [PLUS] | MINUS PAR_L REG
    // RAR_R
    // | HASH (NUM | IDENT) | IDENT | NUM

    public static final int REGISTER = 001;
    public static final int INDIRECT = 002;
    public static final int PRE_DEC = 004;
    public static final int POST_INC = 010;
    public static final int IMM = 020;
    public static final int LABEL = 040;

    private int type;
    private Sep3asmToken ref;

    public Operand(Sep3asmParseContext ctx) {
    }

    public static boolean isFirst(Sep3asmToken tk) {
        return tk.getType() == Sep3asmToken.TK_NUM || tk.getType() == Sep3asmToken.TK_IDENT
                || tk.getType() == Sep3asmToken.TK_HASH || tk.getType() == Sep3asmToken.TK_REG
                || tk.getType() == Sep3asmToken.TK_PAR_L || tk.getType() == Sep3asmToken.TK_PAR_R
                || tk.getType() == Sep3asmToken.TK_MINUS;
    }

    public void parse(Sep3asmParseContext ctx) throws FatalErrorException {
        Sep3asmTokenizer ct = ctx.getTokenizer();
        Sep3asmToken tk = ct.getCurrentToken(ctx);
        switch (tk.getType()) {
            case Sep3asmToken.TK_NUM:
            case Sep3asmToken.TK_IDENT:
                type = LABEL;
                ref = tk;
                break;
            case Sep3asmToken.TK_HASH:
                tk = ct.getNextToken(ctx);
                if (tk.getType() == Sep3asmToken.TK_NUM || tk.getType() == Sep3asmToken.TK_IDENT) {
                    type = IMM;
                    ref = tk;
                } else {
                    ctx.warning("number or identifier is expected after " + tk.toExplainString());
                }
                break;
            case Sep3asmToken.TK_REG:
                type = REGISTER;
                ref = tk;
                break;
            case Sep3asmToken.TK_PAR_L:
                tk = ct.getNextToken(ctx);
                if (tk.getType() == Sep3asmToken.TK_REG) {
                    ref = tk;
                    ct.getNextToken(ctx);
                    tk = ct.getNextToken(ctx);
                    if (tk.getType() == Sep3asmToken.TK_PLUS) {
                        type = POST_INC;
                    } else {
                        type = INDIRECT;
                        return;
                    }
                }
            case Sep3asmToken.TK_MINUS:
                type = PRE_DEC;
                tk = ct.getNextToken(ctx);
                if (tk.getType() == Sep3asmToken.TK_PAR_L) {
                    tk = ct.getNextToken(ctx);
                    if (tk.getType() == Sep3asmToken.TK_REG) {
                        ref = tk;
                        ct.getNextToken(ctx);
                    } else {
                        ctx.error("register is expected after " + tk.toExplainString());
                    }
                } else {
                    ctx.error("unexpected token after " + tk.toExplainString());
                }
                break;
            default:
        }
        ct.getNextToken(ctx);
    }

    private int fiveBits;
    private boolean needsExtraWord;
    private int extraWord;
    private int registerNum;

    public boolean needsExtraWord() {
        return needsExtraWord;
    }

    public int to5bits() {
        return fiveBits;
    }

    public int getExtraWord() {
        return extraWord;
    }

    public void pass1(Sep3asmParseContext ctx) throws FatalErrorException {
        if (ref.getType() == Sep3asmToken.TK_REG) {
            registerNum = ref.getRegisterNumber();
        }
        if (type == IMM || type == LABEL) {
            needsExtraWord = true;
            registerNum = 7;
        }
    }

    public void limit(int info, Sep3asmParseContext ctx, Sep3asmToken inst, boolean isToOperand) {
        if ((info & type) == 0) {
            ctx.error("operand " + ref.toExplainString() + " is not allowed for " + inst.toExplainString());
        }
        if (type == PRE_DEC && registerNum != 6) {
            ctx.error("operand " + ref.toExplainString() + " is not allowed for " + inst.toExplainString());
        }
        if ((type == INDIRECT || type == POST_INC) && isToOperand && registerNum == 7) {
            ctx.error("operand " + ref.toExplainString() + " is not allowed for " + inst.toExplainString());
        }
    }

    public void pass2(Sep3asmParseContext ctx) throws FatalErrorException {
        if (needsExtraWord) {
            if (ref.getType() == Sep3asmToken.TK_IDENT) {
                LabelEntry le = ctx.getSymbolTable().search(ref.getText());
                if (le == null || !le.isLabel()) {
                    ctx.error("undefined label: " + ref.toExplainString());
                    return;
                }
                extraWord = le.getInteger();
            } else {
                extraWord = ref.getIntValue();
            }
        }
        switch (type) {
            case REGISTER:
                fiveBits = 0;
                break;
            case INDIRECT:
                fiveBits = 1;
                break;
            case PRE_DEC:
                fiveBits = 2;
                break;
            case IMM:
            case LABEL:
            case POST_INC:
                fiveBits = 3;
                break;
            default:
                fiveBits = 0;
                break;
        }
        fiveBits = (fiveBits << 3) | registerNum;
    }
}
