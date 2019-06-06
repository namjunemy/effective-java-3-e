package item05;

import common.TestDescription;
import lombok.Getter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 아이템 5 : 자원을 직접 명시하지 말고, 의존 객체 주입을 사용하라
 * <p>
 * 클래스가 내부적으로 하나 이상의 자원에 의존하고, 그 자원이 클래스 동작에 영향을 준다면 싱글턴과
 * 정적 유틸리티 클래스는 사용하지 않는 것이 좋다. 이 자원들은 클래스가 직접 만들게 해서도 안된다.
 * 대신 필요한 자원을 (혹은 그 자원을 만들어주는 팩터리를) 생성자에 (혹은 정적 팩터리나 빌더에) 넘겨주자.
 * 의존 객체 주입이라하는 이 기법은 클래스의 유연성, 재사용성, 테스트 용이성을 기막히게 개선해준다.
 */
public class Item05Test {

    /*
    사용하는 자원에 따라 동작이 달라지는 클래스에는 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않다.
    클래스가 여러 자원 인스턴스를 지원해야 하며, 클라이언트가 원하는 자원을 사용해야 한다.
    이 조건을 만족하는 간단한 패턴이 인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 방식이다.

    이 패턴의 쓸만한 변형으로, 생성자에 자원 팩터리를 넘겨주는 방식이 있다.
    패겉리란 호출할 때마다 특정 타입의 인스턴스를 반복해서 만들어주는 객체를 말한다.
    자바 8에서 소개한 Supplier<T> 인터페이스가 팩터리를 표현한 완벽한 예다.
    Supplier<T>를 입력으로 받는 메서드는 일반적으로 한정적 와일드카드 타입을 사용해 팩터리의 타입 매개변수를 제한해야 한다.
    이 방식을 사용해 클라이언트는 자신이 명시한 타입의 하위 타입이라면 무엇이든 생성할 수 있는 팩터리를 넘길 수 있다.
     */
    @Test
    @TestDescription("클라이언트가 제공한 팩터리가 생성한 타일(Tile)들로 구성된 모자이크를 만드는 메서드")
    public void test() {
        Mosaic mosaic = new Mosaic();
        mosaic.create(ATile::new);
        mosaic.create(BTile::new);
        mosaic.create(ATile::new);
        mosaic.create(BTile::new);

        assertThat(mosaic.getTiles().size()).isEqualTo(4);
    }

    @Getter
    class Mosaic {
        private List<Tile> tiles;

        Mosaic() {
            tiles = new ArrayList<>();
        }

        Mosaic create(Supplier<? extends Tile> tileFactory) {
            this.tiles.add(tileFactory.get());
            return this;
        }
    }

    interface Tile {

    }

    class ATile implements Tile {

    }

    class BTile implements Tile {

    }
}
