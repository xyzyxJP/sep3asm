package lang.sep3asm.parse;

import lang.FatalErrorException;
import lang.sep3asm.Sep3asmParseContext;
import lang.sep3asm.Sep3asmParseRule;
import lang.sep3asm.Sep3asmToken;
import lang.sep3asm.Sep3asmTokenizer;
import lang.sep3asm.instruction.Sep3Instruction;

public class Inst1B extends Sep3asmParseRule {
    // inst1b ::= INST1B operand
    // ex. JMP LABEL

    private Sep3asmToken inst;
    private Operand op1;
    private Sep3Instruction sep3inst;

    public Inst1B(Sep3asmParseContext ctx) {
    }

    public static boolean isFirst(Sep3asmToken tk) {
        return tk.getType() == Sep3asmToken.TK_INST1B;
    }

    public void parse(Sep3asmParseContext ctx) throws FatalErrorException {
        Sep3asmTokenizer ct = ctx.getTokenizer();
        inst = ct.getCurrentToken(ctx);
        Sep3asmToken tk = ct.getNextToken(ctx);
        if (Operand.isFirst(tk)) {
            op1 = new Operand(ctx);
            op1.parse(ctx);
        } else {
            ctx.warning("operand is expected after " + tk.toExplainString());
        }
    }

    public void pass1(Sep3asmParseContext ctx) throws FatalErrorException {
        sep3inst = inst.getInstruction();
        if (op1 != null) {
            op1.pass1(ctx);
            op1.limit(sep3inst.getOp1Info(), ctx, inst, false);
            if (op1.needsExtraWord()) {
                ctx.addLocationCounter(2);
            } else {
                ctx.addLocationCounter(1);
            }
        }
    }

    public void pass2(Sep3asmParseContext ctx) throws FatalErrorException {
        if (op1 != null) {
            op1.pass2(ctx);
        }
        sep3inst.generate(ctx, op1, null);
    }
}
