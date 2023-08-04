package lang.sep3asm.parse;

import lang.FatalErrorException;
import lang.sep3asm.Sep3asmParseContext;
import lang.sep3asm.Sep3asmParseRule;
import lang.sep3asm.Sep3asmToken;
import lang.sep3asm.Sep3asmTokenizer;
import lang.sep3asm.instruction.Sep3Instruction;

public class Inst1A extends Sep3asmParseRule {
    // inst1a ::= INST1A operand
    // ex. CLR R1

    private Sep3asmToken inst;
    private Operand op2;
    private Sep3Instruction sep3inst;

    public Inst1A(Sep3asmParseContext ctx) {
    }

    public static boolean isFirst(Sep3asmToken tk) {
        return tk.getType() == Sep3asmToken.TK_INST1A;
    }

    public void parse(Sep3asmParseContext ctx) throws FatalErrorException {
        Sep3asmTokenizer ct = ctx.getTokenizer();
        inst = ct.getCurrentToken(ctx);
        Sep3asmToken tk = ct.getNextToken(ctx);
        if (Operand.isFirst(tk)) {
            op2 = new Operand(ctx);
            op2.parse(ctx);
        } else {
            ctx.warning("operand is expected after " + tk.toExplainString());
        }
    }

    public void pass1(Sep3asmParseContext ctx) throws FatalErrorException {
        sep3inst = inst.getInstruction();
        if (op2 != null) {
            op2.pass1(ctx);
            op2.limit(sep3inst.getOp2Info(), ctx, inst, true);
        }
        ctx.addLocationCounter(1);
    }

    public void pass2(Sep3asmParseContext ctx) throws FatalErrorException {
        if (op2 != null) {
            op2.pass2(ctx);
        }
        sep3inst.generate(ctx, null, op2);
    }
}
