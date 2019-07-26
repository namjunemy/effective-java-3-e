package chap03.item11;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.Test;

/*
아이템 11 - equals를 재정의하려거든 hashCode도 재정의하라

equals를 재정의할 때는 hashCode도 반드시 재정의해야 한다. 그렇지 않으면 프로그램이 제대로 동작하지 않을 것이다.
재정의한 hashCode는 Object의 API 문서에 기술된 일반 규약을 따라야 하며,
서로 다른 인스턴스라면 되도록 해시코드도 서로 다르게 구현해야 한다.
이렇게 구현하기가 어렵지는 않지만 조금 따분한 일이긴 하다.
아이템 10에서 봤던 AutoValue 프레임워크를 사용하면 멋진 equals와 hashCode를 자동으로 만들어준다.
IDE들도 이런 기능을 일부 제공한다.

## hashCode의 규약
- equals 비교에 사용되는 정보가 변경되지 않았다면, 객체의 hashcode 메서드는 몇번을 호출해도 항상 일관된 값을 반환해야 한다.
  (단, application을 다시 실행한다면 값이 달라져도 상관없다)
- equals 메서드를 통해 두 개의 객체가 같다고 판단했다면, 두 객체는 똑같은 hashcode 값을 반환해야 한다.
- equals 메서드가 두 개의 객체를 다르다고 판단했다 하더라도, 두 객체의 hashcode가 서로 다른 값을 가질 필요는 없다.(Hash Collision)
  단, 다른 객체에 대해서는 다른 값을 반환해야 해시 테이블의 성능이 좋아진다.

## 좋은 해시 함수 만들기
좋은 해시 함수는 서로 다른 인스턴스에 대해 다른 해시코드를 반환한다.
이게 바로 hashcode의 세번째 규약이 요구하는 속성이다.
이상적인 해시 함수는 주어진(서로 다린) 인스턴스들을 32비트 정수 범위에 균일하게 분배해야 한다.

@Override
public int hashCode() {
    int c = 31;
    //1. int변수 result를 선언한 후 첫번째 핵심 필드에 대한 hashcode로 초기화 한다.
    int result = Integer.hashCode(firstNumber);

    //2. 기본타입 필드라면 Type.hashCode()를 실행한다
    //Type은 기본타입의 Boxing 클래스이다.
    result = c * result + Integer.hashCode(secondNumber);

    //3. 참조타입이라면 참조타입에 대한 hashcode 함수를 호출 한다.
    //4. 값이 null이면 0을 더해 준다.
    result = c * result + address == null ? 0 : address.hashCode();

    //5. 필드가 배열이라면 핵심 원소를 각각 필드처럼 다룬다.
    for (String elem : arr) {
      result = c * result + elem == null ? 0 : elem.hashCode();
    }

    //6. 배열의 모든 원소가 핵심필드이면 Arrays.hashCode를 이용한다.
    result = c * result + Arrays.hashCode(arr);

    //7. result = 31 * result + c 형태로 초기화 하여
    //result를 리턴한다.
    return result;
}

hashcode를 다 구현했다면, 이 메서드가 동치인 인스턴스에 대해 똑같은 해시코드를 반환할지 TestCase를 작성해보자
파생필드는 hashcode 계산에서 제외해도 된다.
equals 비교에 사용되지 않는 필드는 반드시 제외한다.
31 * result를 곱하는 순서에 따라 result 값이 달라진다.
곱할 숫자를 31로 지정하는 이유는 31이 홀수 이면서 소수(prime)이기 때문이다.
2를 곱하는 것은 shift연산과 같은 결과를 내기 떄문에 추천하지 않는다.
31을 이용하면 (i << 5) - i와 같이 최적화 할 수 있다.

## hashcode를 편하게 만들어 주는 모듈
- Objects.hash()
  내부적으로 AutoBoxing이 일어나 성능이 떨어진다.
- Lombok의 @EqualsAndHashCode
- Google의 @AutoValue


## hashcode를 재정의 할 때 주의 할 점!
- 불변 객체에 대해서는 hashcode 생성비용이 많이 든다면, hashcode를 캐싱하는 것도 고려하자
  - 스레드 안전성까지 고려해야 한다.
- 성능을 높인답시고 hashcode를 계산할 떄 핵심필드를 생략해서는 안된다.
  - 속도는 빨라지겠지만, hash품질이 나빠져 해시테이블 성능을 떨어뜨릴 수 있다 (Hashing Collision)
- hashcode 생성규칙을 API사용자에게 공표하지 말자
  - 그래야 클라이언트가 hashcode값에 의지한 코드를 짜지 않는다.
  - 다음 릴리즈 시, 성능을 개선할 여지가 있다.

## Reference
- https://jaehun2841.github.io/2019/01/12/effective-java-item11
 */
public class Item11Test {

    @Test
    public void hashcode_재정의() {
        HashMap<ExtendedPhoneNumber, String> map = new HashMap<>();
        map.put(new ExtendedPhoneNumber(707, 867, 5307), "제니");
        System.out.println("Instance 1 hashcode : " + new ExtendedPhoneNumber(707, 867, 5307).hashCode());
        System.out.println("Instance 2 hashcode : " + new ExtendedPhoneNumber(707, 867, 5307).hashCode());
        assertEquals(map.get(new ExtendedPhoneNumber(707, 867, 5307)), "제니");
    }

    @Test
    public void hashcode_재정의안함() {
        HashMap<PhoneNumber, String> map = new HashMap<>();
        map.put(new PhoneNumber(707, 867, 5307), "제니");
        System.out.println("Instance 1 hashcode : " + new PhoneNumber(707, 867, 5307).hashCode());
        System.out.println("Instance 2 hashcode : " + new PhoneNumber(707, 867, 5307).hashCode());
        assertNotEquals(map.get(new PhoneNumber(707, 867, 5307)), "제니");
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhoneNumber {
        protected int firstNumber;
        protected int secondNumber;
        protected int thirdNumber;

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof PhoneNumber)) {
                return false;
            }

            PhoneNumber p = (PhoneNumber) o;
            return this.firstNumber == p.firstNumber &&
                this.secondNumber == p.secondNumber &&
                this.thirdNumber == p.thirdNumber;
        }
    }

    @NoArgsConstructor
    public static class ExtendedPhoneNumber extends PhoneNumber {

        public ExtendedPhoneNumber(int firstNumber, int secondNumber, int thirdNumber) {
            super(firstNumber, secondNumber, thirdNumber);
        }

        @Override
        public int hashCode() {
            int c = 31;
            int hashcode = Integer.hashCode(firstNumber);
            hashcode = c * hashcode + Integer.hashCode(secondNumber);
            hashcode = c * hashcode + Integer.hashCode(thirdNumber);
            return hashcode;
        }
    }
}
