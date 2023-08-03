package lang.sep3asm.parse;

import lang.FatalErrorException;
import lang.sep3asm.Sep3asmParseContext;
import lang.sep3asm.Sep3asmParseRule;
import lang.sep3asm.Sep3asmToken;

public class Line extends Sep3asmParseRule {
    // line ::= NL | labeledLine NL | instLine NL | defLine NL

    public Line(Sep3asmParseContext ctx) {
    }

    public static boolean isFirst(Sep3asmToken tk) {
        return false;
    }

    public void parse(Sep3asmParseContext ctx) throws FatalErrorException {
    }

    public void pass1(Sep3asmParseContext ctx) throws FatalErrorException {
    }

    public void pass2(Sep3asmParseContext pcx) throws FatalErrorException {
    }
}
