package chap03.item10;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Objects;
import org.junit.Test;

/*
아이템 10 - equals는 일반 규약을 지켜 재정의하라

꼭 필요한 경우가 아니면 equals를 재정의하지 말자.
많은 경우에 Object의 equals가 여러분이 원하는 비교를 정확히 수행해준다.
재정의해야 할 때는 그 클래스의 핵심 필드 모두를 빠짐없이, 다섯 가지 규약을 확실히 지켜가며 비교해야 한다.

1. 반사성
- null이 아닌 모든 참조 값 x에 대해 x.equals(x)를 만족해야 한다.
  단순히 말하면 객체는 자기 자신과 비교했을 때 같아야 한다는 뜻이다.

2. 대칭성
- null이 아닌 모든 참조 값 x, y에 대해 x.equals(y)가 true 이면, y.equals(x)가 true를 만족해야 한다.

3. 추이성
- null이 아닌 모든 참조 값 x, y, z에 대해 x.equals(y)가 true이고,
  y.equals(z)가 true이면, x.equals(z)도 true가 되야 한다는 조건이다.

4. 일관성
- null이 아닌 모든 참조 값 x, y에 대해 x.equals(y)를 반복해서 호출하면 항상 true를 반환하거나 항상 false를 반환한다.

5. Not NUll
- null이 아닌 모든 참조 값 x에 대해, x.equals(null)은 false다.
  기본적으로 x.equals(null)dl true가 되는 일은 생각하기 어렵다.


## equals 구현 절차
- 연산자를 사용해 입력된 파라미터와 자기자신이 같은 객체인지 검사한다. (동일성 검사 - Object Identity)
    - 성능 향상을 위한 코드
    - equals가 복잡할 때 같은 참조를 가진 객체에 대한 비교를 안하기 위함
- instanceof 연산자로 파라미터의 타입이 올바른지 체크
    - 묵시적 null체크 용도로도 사용
    - equals중에서는 같은 interface를 구현한 클래스끼리도 비교하는 경우가 있다.
- 입력을 올바른 타입으로 형변환한다.
    - Object타입의 파라미터를 비교하고자 하는 타입으로 형변환한다.
    - 앞서 instanceof 연산을 수행했기 때문에 100% 성공한다.
- 파라미터 Object 객체와 자기자신의 대응되는 핵심필드들이 모두 일치하는지 확인한다.
    - 하나라도 다르면 false를 리턴
    - 만약 interface 기반의 비교가 필요하다면 필드정보를 가져오는 메서드가 interface에 정의되어있어야하고,
    - 구현체 클래스에서는 메서드를 재정의 해야한다.
- float, double을 제외한 기본타입은 ==을 통해 비교
- 참조(reference) 타입은 equals를 통해 비교
- float, double은 Float.compare(float, float)와 Double.compare(double, double)로 비교한다.
    - Float.Nan, -0.0f등을 비교하기 위함이다.
    - 이 메서드들은 float -> Float, double -> Double로 변환하는 오토박싱 기능이 수반되므로 성능상 좋지 못하다.
- 배열의 모든 원소가 핵심 필드라면 Arrays.equals를 사용하자
- null이 의심되는 필드는 Objects.equals(obj, obj)를 이용해 NullPointerException을 예방하자
- 성능을 올리고자 한다면
    - 다를 확률이 높은 필드부터 비교한다.
    - 비교하는 비용(시간복잡도)이 적은 비교를 먼저 수행
 */
public class Item10Test {
    private final class CaseInsensitiveString {

        private final String s;

        public CaseInsensitiveString(String s) {
            this.s = Objects.requireNonNull(s);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof CaseInsensitiveString) {
                return s.equalsIgnoreCase(((CaseInsensitiveString) o).s);
            }

            if (o instanceof String) { //한 방향으로만 작동!! 같은 CaseInsensitiveString 타입인 경우에만 비교하도록 한다.
                return s.equalsIgnoreCase((String) o);
            }
            return false;
        }
    }

    @Test
    public void 대칭성() {
        CaseInsensitiveString caseInsensitiveString = new CaseInsensitiveString("test");
        String test = "test";

        assertTrue(caseInsensitiveString.equals(test));
        assertFalse(test.equals(caseInsensitiveString));
    }

    class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof Point)) return false;
            Point p = (Point) o;
            return this.x == p.x && this.y == p.y;
        }
    }

    class ColorPoint extends Point {

        private final Color color;

        public ColorPoint(int x, int y, Color color) {
            super(x, y);
            this.color = color;
        }

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof ColorPoint)) return false;

            return super.equals(o) && this.color == ((ColorPoint) o).color; // 대칭성 위배, 추이성 위배
        }
    }
}
