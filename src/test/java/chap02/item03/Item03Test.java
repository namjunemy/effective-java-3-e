package chap02.item03;

import static org.junit.Assert.assertEquals;

import common.TestDescription;
import org.junit.Test;

/**
 * 아이템 3 : private 생성자나 열거 타입으로 싱글턴임을 보증하라
 *
 * 싱글턴이란 인스턴스를 오직 하나만 생성할 수 있는 클래스를 말한다. 싱글턴을 만드는 방법은 아래와 같다.
 */
public class Item03Test {

    /*
    1. private 생성자 + static 객체로 싱글턴 객체를 만드는 방법

    private 생성자를 통해 내부에서만 객체를 생성할 수 있도록 하고
    public static final 키워드를 이용해 static 변수로 1개의 인스턴스만 제공하는 방식

    리플렉션 API AccessibleObject.setAccessible 을 사용해서 private 생성자를 호출할 수 있다.
    이 문제를 방어하려면 생성자에 예외처리를 하면된다.
     */
    @Test
    public void privateStatic() {
        Elvis elvis1 = ElvisObjectByPrivateStatic.INSTANCE;
        Elvis elvis2 = ElvisObjectByPrivateStatic.INSTANCE;

        assertEquals(elvis1, elvis2);
    }

    /*
    2. 정적 팩터리 메서드를 제공하는 방식

    장점
    - 향후 필요에 따라 변경될 수 있는 확장성을 가지고 있다. API의 변경없이 싱글턴이 아니게
    - 원한다면 제네릭 싱글턴 팩터리로 만들 수 있다.
    - 메서드 참조 Elvis::getInstance 를 공급자 Supplier<Elvis>로 사용할 수 있다.

    1, 2번 방식의 단점
    - 만든 싱글턴 클래스를 직렬화하려면 단순히 Serializable을 구현한다고 선언하는 것만으로는 부족하다.
    모든 인스턴스 필드를 일시적(transient)이라고 선언하고 readResolve 메서드를 제공해야 한다.
    이렇게 하지 않으면 역직렬화할 경우마다 새로운 인스턴스를 생성하게 된다.
    => SerializableElvisObject.class 참조

    - 리플렉션 API AccessibleObject.setAccessible 을 사용해서 private 생성자를 호출할 수 있다.
    - 이 문제를 방어하려면 생성자에 예외처리를 하면된다.
     */
    @Test
    @TestDescription("2. 정적 팩터리 메서드를 제공하는 방식")
    public void factoryMethod() {
        Elvis elvis1 = ElvisObjectByFactory.getInstance();
        Elvis elvis2 = ElvisObjectByFactory.getInstance();

        assertEquals(elvis1, elvis2);
    }

    /*
    3. 열거 타입(enum)을 이용한 싱글턴 객체 생성

    복잡한 직렬화 상황이나, 리플렉션 공격에도 안전한
     */
    @Test
    public void enumTest() {
        ElvisObjectByEnum elvis1 = ElvisObjectByEnum.INSTANCE;
        ElvisObjectByEnum elvis2 = ElvisObjectByEnum.INSTANCE;

        assertEquals(elvis1, elvis2);
    }
}
