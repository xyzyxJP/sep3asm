package lang.sep3asm;

import lang.IOContext;
import lang.SimpleParseContext;

public class Sep3asmParseContext extends SimpleParseContext {
    private Sep3asmSymbolTable symTbl;
    public boolean hasDotEnd;

    public Sep3asmParseContext(IOContext ioCtx, Sep3asmTokenizer tknz) {
        super(ioCtx, tknz);
        symTbl = new Sep3asmSymbolTable();
        hasDotEnd = false;
    }

    @Override
    public Sep3asmTokenizer getTokenizer() {
        return (Sep3asmTokenizer) super.getTokenizer();
    }

    public Sep3asmSymbolTable getSymbolTable() {
        return symTbl;
    }

    private int locationCounter = 0;

    public int getLocationCounter() {
        return locationCounter;
    }

    public void addLocationCounter(int n) {
        locationCounter += n;
    }

    public void setLocationCounter(int n) {
        locationCounter = n;
    }

    public void output(int n) {
        getIOContext().getOutStream().printf("%04X:%04X\n", 0xFFFF & locationCounter, 0xFFFF & n);
        addLocationCounter(1);
    }
}
