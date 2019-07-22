package chap02.item01;

import common.TestDescription;
import lombok.AllArgsConstructor;
import org.junit.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 아이템 1 : 생성자 대신 정적 팩터리 메서드를 고려하라
 *
 * 요약
 * 정적 팩터리 메서드와 public 생성자는 각자의 쓰임새가 있으니 상대적인 장단점을 이해하고 사용하는 것이 좋다.
 * 그렇다고 하더라도 정적 팩터리 메서드를 사용하는 게 유라한 경우가 더 많으므로
 * 무작정 public 생성자를 제공하던 습관이 있다면 고치자.
 */
public class Item01Test {

    @Test
    public void booleanTest() {
        // valueOf 의 내부 구현을 보자
        // - Boolean 이라는 이름을 가질 수 있어서, 반환될 객체의 특성을 제대로 설명할 수 있다.
        // - 호출할 때마다 인스턴스를 생성하지 않아도 된다.
        assertTrue(Boolean.valueOf(true));
        assertFalse(Boolean.valueOf(false));
    }

    @Test
    @TestDescription("흔히 쓰는 정적 팩토리 메서드 명명 방식들")
    public void namingRulesOfStaticFactoryMethod() {
        // from - 매개변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 형변환 메서드
        Date date = Date.from(Instant.now());

        // of - 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드
        Set<Person> integers = EnumSet.of(Person.A, Person.B, Person.C, Person.A);
        assertThat(integers.size()).isEqualTo(3);

        // valueOf - from과 of의 더 자세한 버전
        BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);
        assertThat(prime.intValue()).isEqualTo(2147483647);

        // instance 혹은 getInstance - 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 보장하지는 않는다.
        // StackWalker luke = StackWalker.getInstance(options);

        // create 혹은 newInstance - instance 혹은 getInstance 와 같지만, 매번 새로운 인스턴스를 생성해 반환함을 보장
        // Object newArray = Array.newInstance(classObject, arrayLen);

        // getType - getInstance 와 같으나 다른 클래스에 팩터리 메서드를 정의할 때 사용
        // FileStore fs = Files.getFileStore(path);

        // newType - newInstance 와 같으나 다른 클래스에 팩터리 메서드를 정의할 때 사용
        // BufferedReader br = Files.newBufferedReader(path);

        // type - getType 과 newType 의 간결한 버전
        // List<Person> persons = Collections.list(enumerationElements);
    }

    @AllArgsConstructor
    private enum Person {
        A, B, C
    }
}
