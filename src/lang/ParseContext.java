package lang;

public abstract class ParseContext {
    // 入出力に関わるメソッド群
    @SuppressWarnings("rawtypes")
    public ParseContext(IOContext ioCtx, Tokenizer tknz) {
        setIOContext(ioCtx);
        setTokenizer(tknz);
    }

    private IOContext ioCtx; // 入出力コンテキスト
    @SuppressWarnings("rawtypes")
    private Tokenizer tknz; // 字句切り出しクラス

    public void setIOContext(IOContext ioCtx) {
        this.ioCtx = ioCtx;
    }

    public IOContext getIOContext() {
        return ioCtx;
    }

    @SuppressWarnings("rawtypes")
    public void setTokenizer(Tokenizer tknz) {
        this.tknz = tknz;
    }

    @SuppressWarnings("rawtypes")
    public Tokenizer getTokenizer() {
        return tknz;
    }

    // エラーの扱いに関するもの
    private int warningNo = 0; // 解析警告数
    private int errorNo = 0; // 解析エラー数

    public void errorReport() {
        ioCtx.getErrStream().println("Error: " + errorNo + ", Warning: " + warningNo);
    }

    private void message(final String s) {
        ioCtx.getErrStream().println(s);
    }

    // エラー（処理系が処理しきれない誤り）
    public boolean hasNoError() {
        return errorNo == 0;
    }

    public void error(final String s) {
        message(s);
        ++errorNo;
    }

    // 本当に致命的な場合は例外を投げる
    public void fatalError(final String s) throws FatalErrorException {
        error(s);
        throw new FatalErrorException();
    }

    // 警告（回復できる些細な誤り）
    public void warning(final String s) {
        message(s);
        ++warningNo;
    }
}