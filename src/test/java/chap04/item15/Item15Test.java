package chap04.item15;

import common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;

/*

Item 15. 클래스와 멤버의 접근 권한을 최소화 하라.

잘 설계된 컴포넌트는 클래스 모든 내부 구현을 완벽히 숨겨 구현과 API를 깔끔하게 분리한다.
오직 API를 통해서만 다른 컴포넌트와 소통하며 서로의 내부 동작 방식에는 전혀 개의치 않는다.
정보은닉, 혹은 캡슐화라고 하는 이 개념은 소프트웨어 설계의 근간이 되는 원리다.


## 정보은닉의 장점

# 시스템 개발 속도를 높인다.
여러가지 컴포넌트를 병렬로 개발할 수 있기 때문이다. 예를 들어, 회원탈퇴 기능을 개발한다고 해보자.

public interface WithdrawalService {
    void withdrawalMember(Member member);
    List<Member> getMembers();
    default void startProcess() {
        List<Member> members = getMembers();
        members.forEach(this::withdrawalMember);
    }
}

위와같은 회원탈퇴 로직에 대한 인터페이스를 제공하고
일반회원, 해외회원, 비회원 탈퇴등의 로직이 상이하려 Service를 별도로 구현한다고 하는경우
Interface 스펙에 맞추어 여러개발자가 개발 할 수 있다.

# 시스템 관리 비용을 낮춘다.
각 컴포넌트를 빨리 파악하여 디버깅 할 수 있고, 다른 컴포넌트로 교체하는 부담도 적다.
위에서 예시로 들은 일반회원 탈퇴, 해외회원 탈퇴, 비회원 탈퇴에서 비회원 탈퇴 로직에 변경이 생긴경우
WithdrawalService 인터페이스를 구현한 비회원 탈퇴 서비스를 만들면 된다.

# 성능 최적화에 도움을 준다
완성된 시스템을 프로파일링해서 최적화할 컴포넌트를 정한 다음 다른 컴포넌트에 영향을 주지 않고 해당 컴포넌트만 최적화할 수 있다.
위에서 예시로 들은 일반회원 탈퇴, 해외회원 탈퇴, 비회원 탈퇴에서 비회원 탈퇴에 대해 처리대상이 많아서
startProcess 메서드를 확장하여 내부적으로 withdrawalMember 메서드를 사용하지 않고 회원 리스트에 대해서
쿼리를 이용해 Insert ~ Select로 한방에 처리 할 수도 있다.

# 소프트웨어 재사용성을 높인다.
외부에 의존하지 않고, 독자적으로 동작할 수 있는 컴포넌트라면, 그 컴포넌트와 함께 개발되지 않은 낯선 환경에서도 유용하게 쓰일 가능성이 크다.
예를 들면 알림톡 서버 API를 호출할 수 있는 인터페이스가 있는 경우 그 인터페이스를 그대로 사용할 수 있다.

# 큰 시스템을 제작하는 난이도를 낮춰준다.
시스템 전체가 완성되지 않은 상태에서도 개별 컴포넌트의 동작을 검증할 수 있다.(아래의 테스트 코드로 확인)


## Java의 접근제한자

private: 멤버를 선언한 톱레벨 클래스에서만 접근할 수 있다.
package-private: 멤버가 소속된 패키지 안의 모든 클래스에서 접근 할 수 있다.
protected: package-private의 접근 범위를 포함하며, 이 멤버를 선언한 클래스의 하위 클래스에서도 접근할 수 있다.
public: 모든 곳에서 접근 할 수 있다.


## 모든 클래스와 멤버의 접근성을 가능한 좁혀야 한다.
- 소프트웨어가 올바로 동작하는 한 항상 가장 낮은 접근 수준을 부여해야 한다.

# 클래스 레벨 접근제한자
톱 레벨 수준(파일명 = 클래스명)이 같은 수준에서의 접근제한자는 public과 package-private만 사용 할 수 있다.
- public 으로 선언하는 경우 공개 API로 사용 : 하위 호환을 평생 신경써야 한다.
- package-private으로 사용하는 경우 해당 패키지 안에서만 사용 가능 : 다음 릴리즈에 내부로직을 변경해도 괜찮다.

# 이너클래스 사용하기
- 한 클래스에서만 사용하는 package-private 톱레밸 클래스나 인터페이스는 사용하는 클래스 안에 private static으로 중첩해보자
- public일 필요가 없는 클래스의 접근 수준을 package-private으로 해보자 : 다른 패키지에서 사용하지 못하게 막아야 한다.

코드로 보자면,

public class Food {
  private String name;
  private int price;
  private List<Shop> shops;
}

public class Shop {
  private String name;
  private String owner;
}

대신에, 이런식으로 사용 할 수 있다.

public class Food {
  private String name;
  private int price;
  private List<Shop> shops;

  private static class Shop {
    private String name;
    private String owner;
  }
}

# private과 package-private은 해당 클래스의 구현에 해당하므로 공개 API에 영향을 주지 않는다.
- 일단 처음에는 모든 멤버는 private으로 만들어야 한다.
- 같은 패키지에서 접근해야 하는 멤버가 있는 경우 package-private으로 변경
- 단, Serialize를 구현한 클래스의 경우 공개 API에 의도치 않게 공개 될 수도 있다.
- 필드의 접근권한을 package-private에서 protected로 바꾸는 순간 필드에 접근 할 수 있는 대상 범위가 늘어나니 주의해야 한다.

# 메서드를 재정의 할 경우에는 접근 수준을 상위 클래스에서보다 좁게 설정 할 수 없다.
- 상위 클래스에서 접근제한자가 protected인데 하위 클래스에서 갑자기 package-private이나 private으로 변경할 수 없다.
- 상위 클래스의 인스턴스는 아휘 클래스의 인스턴스로 대체해 사용할 수 있어야 한다는 규칙(리스코프 치환원칙)을 위반하기 때문이다.
- 단, 인터페이스를 구현하는 경우에는 클래스의 메서드는 모두 public으로 해야 한다.

# 코드를 테스트 하려는 목적으로 클래스, 인터페이스의 접근 범위를 넓히는 것을 주의하라
- public 클래스의 private을 package-private으로 바꾸는 것은 괜찮다. 하지만 그 이상의 경우 공개 API에 문제가 될 수 있다.


## public 클래스의 인스턴스 필드는 되도록 public이 아니어야 한다.
필드가 가변 객체를 참조하거나(Collections나 배열), final이 아닌 인스턴스 필드를 public으로 선언하면 불변을 보장할 수 없다.
public 가변 필드를 갖는 클래스는 일반적으로 thread-safe 하지 않다.
내부 구현을 바꾸고 싶어도 public 필드를 없애는 방식으로는 리팩토링이 불가능하다.

하지만 상수라면 관례대로 public static final 필드로 공개해도 좋다.
필드명 네이밍은 관례상 대문자와 언더바의 혼합으로 구성한다. 그리고 반드시 불변 객체를 참조하도록 한다.
불변겅이 깨자는 순간 어마무시한 일이 일어난다.


## 클래스에서 public static final 배열 필드를 두지 말아라

public static final Thing[] VALUES = {...};

이런 경우에 VALUES에 대한 참조를 변경할 수는 없지만, 배열내의 내용을 변경하는 것은 가능하다.

# 해결책 1. Thing 배열을 private으로 만들고 public 불변 리스트를 추가해서 해결할 수 있다.

private static final Thing[] PRIVATE_VALUES = {...};
public static final List<Thing> VALUES = Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));

# 해결책 2. Thing 배열을 private으로 만들고 public 메서드를 추가한다.

private static final Thing[] PRIVATE_VALUES = {...};
public static final Thing[] values() {
  return PRIVATE_VALUES.clone(); //방어적 복사본
}


## 자바 9에 추가된 Modules (Project Jigsaw)


## 핵심정리
public 클래스는 절대 가변 필드를 직접 노출해서는 안된다.
불변 필드라면 노출해도 덜 위험하지만 완전히 안심할 수는 없다.
package-private 클래스나 private 중첩 클래스에서는 종종 (불변이든 가변이든) 필드를 노출하는 편이 나을 떄도 있다.

reference - https://jaehun2841.github.io/2019/01/19/effective-java-item15

 */

@RunWith(MockitoJUnitRunner.class)
public class Item15Test {

    private class TestWithDrawalService implements WithdrawalService {

        @Override
        public String withdrawalMember(Member member) {
            return null;
        }

        @Override
        public List<Member> getMembers() {
            return null;
        }
    }

    @Mock
    private TestWithDrawalService testWithDrawalService;

    @Test
    @TestDescription("인터페이스 구현체가 완성되지 않은 상태에서도 컴포넌트의 동작을 검증할 수 있다.")
    public void 컴포넌트_동작_검증() {
        Member member = Member.builder()
            .name("NJ")
            .age(28)
            .build();

        // given
        doReturn(Arrays.asList(member)).when(testWithDrawalService).getMembers();
        doCallRealMethod().when(testWithDrawalService).startProcess();

        // when
        String result = testWithDrawalService.startProcess();

        //then
        assertThat(result).isEqualTo("FINISHED");
    }
}
