package chap02.item03;

import java.util.function.Supplier;

public class ElvisObjectByFactory implements Elvis {

    private static final ElvisObjectByFactory INSTANCE = new ElvisObjectByFactory();

    private ElvisObjectByFactory() {
    }

    public static ElvisObjectByFactory getInstance() {
        return INSTANCE;
    }

    public static Supplier<Elvis> get() {
        return () -> INSTANCE;
    }
}
