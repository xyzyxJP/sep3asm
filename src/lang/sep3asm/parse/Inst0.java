package lang.sep3asm.parse;

import lang.FatalErrorException;
import lang.sep3asm.Sep3asmParseContext;
import lang.sep3asm.Sep3asmParseRule;
import lang.sep3asm.Sep3asmToken;
import lang.sep3asm.Sep3asmTokenizer;
import lang.sep3asm.instruction.Sep3Instruction;

public class Inst0 extends Sep3asmParseRule {
    // inst0 ::= INST0
    // ex. HLT

    private Sep3asmToken inst;
    private Sep3Instruction sep3inst;

    public static boolean isFirst(Sep3asmToken tk) {
        return tk.getType() == Sep3asmToken.TK_INST0;
    }

    public void parse(Sep3asmParseContext ctx) throws FatalErrorException {
        Sep3asmTokenizer ct = ctx.getTokenizer();
        inst = ct.getCurrentToken(ctx);
        ct.getNextToken(ctx);
    }

    public void pass1(Sep3asmParseContext ctx) throws FatalErrorException {
        sep3inst = inst.getInstruction();
        ctx.addLocationCounter(1);
    }

    public void pass2(Sep3asmParseContext ctx) throws FatalErrorException {
        sep3inst.generate(ctx, null, null);
    }
}
