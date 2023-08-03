package lang.sep3asm;

import lang.SimpleToken;
import lang.SimpleTokenizer;
import lang.sep3asm.instruction.Sep3Instruction;

public class Sep3asmTokenizer extends SimpleTokenizer {
    private Sep3asmTokenRule rule;
    private Sep3asmToken currentToken = null;

    public Sep3asmTokenizer(Sep3asmTokenRule rule) {
        this.rule = rule;
        setSpaceChars(" \t\r");
        setAlphaChars("._");
        setCommentChar(';');
        useHexNumber(true); // 16進数（0xNNNN）を有効にする
        useOctalNumber(false); // 8進数（0NNNN） を無効にする
        currentToken = null;
    }

    public Sep3asmToken getCurrentToken(Sep3asmParseContext pcx) {
        return currentToken;
    }

    public Sep3asmToken getNextToken(Sep3asmParseContext pcx) {
        TokenAssoc ta = null;
        SimpleToken tk = super.getNextToken(pcx);
        int type = tk.getType();
        // Sep3asmTokenRule によるトークンの種類の判定
        if ((ta = (TokenAssoc) rule.get(tk.getText())) != null) {
            type = ta.getType();
        }
        currentToken = new Sep3asmToken(type, tk, ta);
        return currentToken;
    }

    public Sep3Instruction getInstruction(String name, Sep3asmParseContext pcx) {
        return null;
    }
}
