package chap02.item04;

import java.time.Instant;
import lombok.experimental.UtilityClass;
import org.junit.Test;

/**
 * 아이템 4 : 인스턴스화를 막으려거든 private 생성자를 사용하라
 *
 * 추상클래스로 만드는 것은 인스턴스화를 막을 수 없다. 단순히 상속을 통해 인스턴스를 만들 수 있기 떄문이다. private 생성자를 이용하자
 */
public class Item04Test {

    // private 생성자를 만드는 것 만으로도 인스턴스화를 막을 수 있다. 외부에서 new 키워드를 통해 인스턴스를 만들 수 없다.
    class TestUtils {

        private TestUtils() {
            throw new AssertionError();
        }
    }

    @Test(expected = AssertionError.class)
    public void testUtilsTest() {
        TestUtils testUtils = new TestUtils();
    }

    /*
    lombok의 @UtilityClass를 사용하면 기본생성자가 private으로 만들어지고,
    리플렉션 혹은 내부에서 생성자를 호출할 경우에는 UnsupportedOperationException이 발생한다.
    Utils 클래스에는 @UtilityClass를 사용하는 것도 좋은 방법이다
     */
    @UtilityClass
    class DateTimeUtils {

        public static Long getNow() {
            return Instant.now().getEpochSecond();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void utilClassTest() {
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
    }
}
