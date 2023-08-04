package lang.sep3asm;

import lang.SimpleToken;
import lang.sep3asm.instruction.Sep3Instruction;

public class Sep3asmToken extends SimpleToken {
    public static final int TK_DOT_WORD = 2; // .WORD
    public static final int TK_MINUS = 3; // -
    public static final int TK_COMMA = 4; // ,
    public static final int TK_NL = 5; // '\n'
    public static final int TK_INST0 = 6; // Zero operand instruction
    public static final int TK_INST1A = 7; // One operand instruction (FROM)
    public static final int TK_INST1B = 8; // One operand instruction (TO)
    public static final int TK_INST2 = 9; // Two operand instruction
    public static final int TK_DOT = 10; // .
    public static final int TK_EQUAL = 11; // =
    public static final int TK_COLON = 12; // :
    public static final int TK_SEMI_COLON = 13; // ;
    public static final int TK_HASH = 14; // #
    public static final int TK_REG = 15; // R0-R7
    public static final int TK_PAR_L = 16; // (
    public static final int TK_PAR_R = 17; // )
    public static final int TK_PLUS = 18; // +
    public static final int TK_DOT_BLOCK = 19; // .BLKW
    public static final int TK_DOT_END = 20; // .END

    public Sep3asmToken(int type, SimpleToken child, TokenAssoc ta) {
        super(type, child.getLineNo(), child.getColumnNo(), child.getText());
        this.info = ta;
    }

    private TokenAssoc info;

    public Sep3Instruction getInstruction() {
        return info.getInstruction();
    }

    public int getRegisterNumber() {
        return info.getRegisterNumber();
    }
}
