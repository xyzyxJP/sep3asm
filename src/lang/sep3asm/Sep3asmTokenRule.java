package lang.sep3asm;

import java.util.HashMap;

import lang.sep3asm.instruction.OneOperandInstruction;
import lang.sep3asm.instruction.RelativeJumpInstruction;
import lang.sep3asm.instruction.TwoOperandInstruction;
import lang.sep3asm.instruction.ZeroOperandInstruction;
import lang.sep3asm.parse.Operand;

public class Sep3asmTokenRule extends HashMap<String, Object> {
    private static final long serialVersionUID = 6928902799089728690L;
    private static final int D = Operand.REGISTER; // レジスタアドレシングを許す
    private static final int I = Operand.INDIRECT; // レジスタ間接アドレシングを許す
    private static final int MI = Operand.PRE_DEC; // プレデクリメント・レジスタ間接アドレシングを許す
    private static final int IP = Operand.POST_INC; // ポストインクリメント・レジスタ間接アドレシングを許す
    private static final int IMM = Operand.IMM; // 即値（イミディエイト）を許す
    private static final int LABEL = Operand.LABEL; // 解析時のみ、ラベルを＃なしに書いてある

    // toオペランドに -(R?)を使うのは禁止
    // fromオペランドに -(R?) を使うときは、R6,SPに限定される
    // toオペランドに (R7), (R7)+ を使うのは禁止

    private static final int FROM_OPS = D | I | MI | IP | IMM;
    private static final int TO_OPS = D | I | IP;

    public Sep3asmTokenRule() {
        put("hlt", new TokenAssoc(Sep3asmToken.TK_INST0, new ZeroOperandInstruction(0x0000, 0, 0)));
        put("clr", new TokenAssoc(Sep3asmToken.TK_INST1A, new OneOperandInstruction(0x1000, 0, TO_OPS)));
        put("asl", new TokenAssoc(Sep3asmToken.TK_INST1A, new OneOperandInstruction(0x2000, 0, TO_OPS)));
        put("asr", new TokenAssoc(Sep3asmToken.TK_INST1A, new OneOperandInstruction(0x2400, 0, TO_OPS)));
        put("lsl", new TokenAssoc(Sep3asmToken.TK_INST1A, new OneOperandInstruction(0x3000, 0, TO_OPS)));
        put("lsr", new TokenAssoc(Sep3asmToken.TK_INST1A, new OneOperandInstruction(0x3400, 0, TO_OPS)));
        put("rol", new TokenAssoc(Sep3asmToken.TK_INST1A, new OneOperandInstruction(0x3800, 0, TO_OPS)));
        put("ror", new TokenAssoc(Sep3asmToken.TK_INST1A, new OneOperandInstruction(0x3C00, 0, TO_OPS)));
        put("mov", new TokenAssoc(Sep3asmToken.TK_INST2, new TwoOperandInstruction(0x4000, FROM_OPS, TO_OPS)));
        // put("jmp", new TokenAssoc(Sep3asmToken.TK_INST1B, new
        // AbsoluteJumpInstruction(0x4407, FROM_OPS | LABEL, 0)));
        put("ret", new TokenAssoc(Sep3asmToken.TK_INST0, new ZeroOperandInstruction(0x4AC7, 0, 0)));
        put("rit", new TokenAssoc(Sep3asmToken.TK_INST0, new ZeroOperandInstruction(0x4EC7, 0, 0)));
        put("add", new TokenAssoc(Sep3asmToken.TK_INST2, new TwoOperandInstruction(0x5000, FROM_OPS, TO_OPS)));
        put("rjp", new TokenAssoc(Sep3asmToken.TK_INST1B, new RelativeJumpInstruction(0x5407, FROM_OPS | LABEL, 0)));
        put("sub", new TokenAssoc(Sep3asmToken.TK_INST2, new TwoOperandInstruction(0x6000, FROM_OPS, TO_OPS)));
        put("cmp", new TokenAssoc(Sep3asmToken.TK_INST2, new TwoOperandInstruction(0x6C00, FROM_OPS, TO_OPS)));
        put("nop", new TokenAssoc(Sep3asmToken.TK_INST0, new ZeroOperandInstruction(0x7000, 0, 0)));
        put("or", new TokenAssoc(Sep3asmToken.TK_INST2, new TwoOperandInstruction(0x8000, FROM_OPS, TO_OPS)));
        put("xor", new TokenAssoc(Sep3asmToken.TK_INST2, new TwoOperandInstruction(0x8400, FROM_OPS, TO_OPS)));
        put("and", new TokenAssoc(Sep3asmToken.TK_INST2, new TwoOperandInstruction(0x8800, FROM_OPS, TO_OPS)));
        put("bit", new TokenAssoc(Sep3asmToken.TK_INST2, new TwoOperandInstruction(0x8C00, FROM_OPS, TO_OPS)));
        // put("jsr", new TokenAssoc(Sep3asmToken.TK_INST1B, new
        // AbsoluteJumpInstruction(0xB01E, FROM_OPS | LABEL, 0)));
        put("rjs", new TokenAssoc(Sep3asmToken.TK_INST1B, new RelativeJumpInstruction(0xB41E, FROM_OPS | LABEL, 0)));
        // put("svc", new TokenAssoc(Sep3asmToken.TK_INST1B, new
        // AbsoluteJumpInstruction(0xB81E, FROM_OPS | LABEL, 0)));
        // put("brn", new TokenAssoc(Sep3asmToken.TK_INST1B, new
        // AbsoluteJumpInstruction(0xC007, FROM_OPS | LABEL, 0)));
        // put("brz", new TokenAssoc(Sep3asmToken.TK_INST1B, new
        // AbsoluteJumpInstruction(0xC407, FROM_OPS | LABEL, 0)));
        // put("brv", new TokenAssoc(Sep3asmToken.TK_INST1B, new
        // AbsoluteJumpInstruction(0xC807, FROM_OPS | LABEL, 0)));
        // put("brc", new TokenAssoc(Sep3asmToken.TK_INST1B, new
        // AbsoluteJumpInstruction(0xCC07, FROM_OPS | LABEL, 0)));
        put("rbn", new TokenAssoc(Sep3asmToken.TK_INST1B, new RelativeJumpInstruction(0xE007, FROM_OPS | LABEL, 0)));
        put("rbz", new TokenAssoc(Sep3asmToken.TK_INST1B, new RelativeJumpInstruction(0xE407, FROM_OPS | LABEL, 0)));
        put("rbv", new TokenAssoc(Sep3asmToken.TK_INST1B, new RelativeJumpInstruction(0xE807, FROM_OPS | LABEL, 0)));
        put("rbc", new TokenAssoc(Sep3asmToken.TK_INST1B, new RelativeJumpInstruction(0xEC07, FROM_OPS | LABEL, 0)));
        put(".word", new TokenAssoc(Sep3asmToken.TK_DOT_WORD, null));
        put("-", new TokenAssoc(Sep3asmToken.TK_MINUS, null));
        put(",", new TokenAssoc(Sep3asmToken.TK_COMMA, null));
        put("\n", new TokenAssoc(Sep3asmToken.TK_NL, null));
        put(".", new TokenAssoc(Sep3asmToken.TK_DOT, null));
        put("=", new TokenAssoc(Sep3asmToken.TK_EQUAL, null));
        put(":", new TokenAssoc(Sep3asmToken.TK_COLON, null));
        put(";", new TokenAssoc(Sep3asmToken.TK_SEMI_COLON, null));
        put("#", new TokenAssoc(Sep3asmToken.TK_HASH, null));
        put("r0", new TokenAssoc(Sep3asmToken.TK_REG, 0));
        put("r1", new TokenAssoc(Sep3asmToken.TK_REG, 1));
        put("r2", new TokenAssoc(Sep3asmToken.TK_REG, 2));
        put("r3", new TokenAssoc(Sep3asmToken.TK_REG, 3));
        put("r4", new TokenAssoc(Sep3asmToken.TK_REG, 4));
        put("r5", new TokenAssoc(Sep3asmToken.TK_REG, 5));
        put("psw", new TokenAssoc(Sep3asmToken.TK_REG, 5));
        put("r6", new TokenAssoc(Sep3asmToken.TK_REG, 6));
        put("sp", new TokenAssoc(Sep3asmToken.TK_REG, 6));
        put("r7", new TokenAssoc(Sep3asmToken.TK_REG, 7));
        put("pc", new TokenAssoc(Sep3asmToken.TK_REG, 7));
        put("(", new TokenAssoc(Sep3asmToken.TK_PAR_L, null));
        put(")", new TokenAssoc(Sep3asmToken.TK_PAR_R, null));
        put("+", new TokenAssoc(Sep3asmToken.TK_PLUS, null));
        put(".blkw", new TokenAssoc(Sep3asmToken.TK_DOT_BLOCK, null));
        put(".end", new TokenAssoc(Sep3asmToken.TK_DOT_END, null));
    }
}
