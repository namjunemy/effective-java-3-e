package item03;

public class ElvisObjectByPrivateStatic implements Elvis {

    public static final ElvisObjectByPrivateStatic INSTANCE = new ElvisObjectByPrivateStatic();

    private ElvisObjectByPrivateStatic() {
    }
}
