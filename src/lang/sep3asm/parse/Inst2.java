package lang.sep3asm.parse;

import lang.FatalErrorException;
import lang.sep3asm.Sep3asmParseContext;
import lang.sep3asm.Sep3asmParseRule;
import lang.sep3asm.Sep3asmToken;
import lang.sep3asm.Sep3asmTokenizer;
import lang.sep3asm.instruction.Sep3Instruction;

public class Inst2 extends Sep3asmParseRule {
    // inst2 ::= INST2 operand COMMA operand

    private Sep3asmToken inst;
    private Operand op1, op2;
    private Sep3Instruction sep3inst;

    public Inst2(Sep3asmParseContext ctx) {
    }

    public static boolean isFirst(Sep3asmToken tk) {
        return tk.getType() == Sep3asmToken.TK_INST2;
    }

    public void parse(Sep3asmParseContext ctx) throws FatalErrorException {
        Sep3asmTokenizer ct = ctx.getTokenizer();
        inst = ct.getCurrentToken(ctx);
        Sep3asmToken tk = ct.getNextToken(ctx);
        if (Operand.isFirst(tk)) {
            op1 = new Operand(ctx);
            op1.parse(ctx);
            tk = ct.getCurrentToken(ctx);
            if (tk.getType() == Sep3asmToken.TK_COMMA) {
                tk = ct.getNextToken(ctx);
            } else {
                ctx.warning("',' is expected after " + tk.toExplainString());
            }
            if (Operand.isFirst(tk)) {
                op2 = new Operand(ctx);
                op2.parse(ctx);
            } else {
                ctx.warning("operand is expected after " + tk.toExplainString());
            }
        } else {
            ctx.warning("operand is expected after " + tk.toExplainString());
        }
    }

    public void pass1(Sep3asmParseContext ctx) throws FatalErrorException {
        sep3inst = ctx.getTokenizer().getInstruction(inst.getText(), ctx);
        if (op1 != null) {
            op1.pass1(ctx);
            op1.limit(sep3inst.getOp1Info(), ctx, inst, false);
            if (op1.needsExtraWord()) {
                ctx.addLocationCounter(2);
            } else {
                ctx.addLocationCounter(1);
            }
        }
        if (op2 != null) {
            op2.pass1(ctx);
            op2.limit(sep3inst.getOp2Info(), ctx, inst, true);
        }
    }

    public void pass2(Sep3asmParseContext ctx) throws FatalErrorException {
        if (op1 != null) {
            op1.pass2(ctx);
        }
        if (op2 != null) {
            op2.pass2(ctx);
        }
        sep3inst.generate(ctx, op1, op2);
    }
}
