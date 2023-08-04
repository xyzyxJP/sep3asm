package lang.sep3asm.parse;

import java.util.ArrayList;

import lang.FatalErrorException;
import lang.sep3asm.Sep3asmParseContext;
import lang.sep3asm.Sep3asmParseRule;
import lang.sep3asm.Sep3asmToken;
import lang.sep3asm.Sep3asmTokenizer;

public class Program extends Sep3asmParseRule {
    // program ::= { line } EOF

    private ArrayList<Sep3asmParseRule> list;

    public Program(Sep3asmParseContext ctx) {
        list = new ArrayList<Sep3asmParseRule>();
    }

    public static boolean isFirst(Sep3asmToken tk) {
        return Line.isFirst(tk) || tk.getType() == Sep3asmToken.TK_EOF;
    }

    @Override
    public void parse(Sep3asmParseContext ctx) throws FatalErrorException {
        Sep3asmTokenizer ct = ctx.getTokenizer();
        Sep3asmToken tk = ct.getCurrentToken(ctx);
        while (Line.isFirst(tk)) {
            Sep3asmParseRule line = new Line(ctx);
            line.parse(ctx);
            list.add(line);
            tk = ct.getCurrentToken(ctx);
        }
        if (tk.getType() != Sep3asmToken.TK_EOF) {
            ctx.warning("There is garbage at the end of the file: " + tk.toExplainString());
        }
    }

    public void pass1(Sep3asmParseContext pcx) throws FatalErrorException {
        for (Sep3asmParseRule line : list) {
            line.pass1(pcx);
        }
    }

    public void pass2(Sep3asmParseContext pcx) throws FatalErrorException {
        for (Sep3asmParseRule line : list) {
            line.pass2(pcx);
        }
    }
}
