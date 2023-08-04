package lang;

import java.util.HashMap;
import java.util.Iterator;

public abstract class SymbolTable<E extends SymbolTableEntry> extends HashMap<String, E> {
    private static final long serialVersionUID = -4113074184770074441L;

    // 登録
    public abstract E register(String name, E e);

    // 検索
    public abstract E search(String name);

    // 全体表示
    public void show() {
        // System.out.println("--- Symbol Table ---");
        for (Iterator<String> i = keySet().iterator(); i.hasNext();) {
            String label = i.next();
            E e = get(label);
            if (e == null) {
                System.out.println(label + ": (null)");
            } else {
                System.out.println(label + ": " + e.toExplainString());
            }
        }
        // System.out.println("------");
    }
}
