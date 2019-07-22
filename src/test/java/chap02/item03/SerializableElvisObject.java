package chap02.item03;

public class SerializableElvisObject implements Elvis {

    private static final Elvis INSTANCE = new SerializableElvisObject();

    private SerializableElvisObject() {
    }

    public static Elvis getInstance() {
        return INSTANCE;
    }

    /*
    싱글턴 클래스를 직렬화 하기 위해서는 Serializable을 구현한다고 선언하는 것 만으로는 부족하다.
    모든 인스턴스 필드에 transient 예약어를 붙여 직렬화를 막은 다음 readResolve 메서드를 제공해야 한다.
    이렇게 하지 않으면 역직렬화 시점에 새로운 인스턴스가 생성 된다.

    역직렬화가 되어 새로운 인스턴스가 생성되더라도,
    클래스간 공유 변수인 static 변수를 이용하면 싱글턴을 보장 할 수 있다.
    새로운 인스턴스는 GC에 의해 UnReachable 형태로 판별되어 제거된다.
     */
    public Object readResolve() {
        return INSTANCE;
    }
}
