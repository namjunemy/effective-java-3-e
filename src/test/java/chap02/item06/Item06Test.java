package chap02.item06;

import common.TestDescription;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * 아이템 6 : 불필요한 객체 생성을 피하라
 */
public class Item06Test {

    @Test
    @TestDescription("String 리터럴과 new String의 차이(https://ict-nroo.tistory.com/18?category=646168)")
    public void stringTest() {
        String a = new String("ball");
        String b = new String("ball");
        String c = "ball";
        String d = "ball";

        // 주소값을 비교
        assertNotSame(a, b);
        assertSame(c, d);
    }

    @Test
    public void booleanTest() {
        Boolean a = new Boolean("true");
        Boolean b = new Boolean("true");
        Boolean c = Boolean.valueOf("true");
        Boolean d = Boolean.valueOf("true");
        Boolean e = Boolean.TRUE;
        Boolean f = Boolean.TRUE;

        // 주소값을 비교
        assertNotSame(a, b);
        assertSame(c, d);
        assertSame(e, f);
    }
}
