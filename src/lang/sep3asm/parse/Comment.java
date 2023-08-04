package lang.sep3asm.parse;

import lang.FatalErrorException;
import lang.sep3asm.Sep3asmParseContext;
import lang.sep3asm.Sep3asmParseRule;
import lang.sep3asm.Sep3asmToken;
import lang.sep3asm.Sep3asmTokenizer;

public class Comment extends Sep3asmParseRule {
    // comment ::= SEMI_COLON [any]
    // ex. ; this is a comment

    public Comment() {
    }

    public static boolean isFirst(Sep3asmToken tk) {
        return tk.getType() == Sep3asmToken.TK_SEMI_COLON;
    }

    public void parse(Sep3asmParseContext ctx) throws FatalErrorException {
        Sep3asmTokenizer ct = ctx.getTokenizer();
        ct.getCurrentToken(ctx);
    }

    public void pass1(Sep3asmParseContext ctx) throws FatalErrorException {
    }

    public void pass2(Sep3asmParseContext ctx) throws FatalErrorException {
    }
}
