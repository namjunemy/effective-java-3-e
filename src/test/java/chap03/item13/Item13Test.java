package chap03.item13;

/*
Item 13. Clone 재정의는 주의해서 진행하라

Cloneable은 복제해도 되는 인터페이스 임을 명시하는 용도의 믹스인 인터페이스이다.
하지만 아쉽게도 의도한 목적을 제대로 이루지 못했다.
가장 큰 문제는 clone메서드가 선언된 곳이 Cloneable이 아닌 Object이고, 그마저도 protected라는데 있다.
그래서 Cloneable을 구현하는 것만으로는 외부에서 clone 메서드를 호출 할 수 없다.

자바의 Cloneable 인터페이스를 보면 아무런 메서드도 없다.
아무것도 없지만, 사실은 Object의 clone메서드의 동작방식을 결정한다.
Cloneable을 구현한 클래스의 인스턴스에서 clone을 호출하면, 그 객체의 필드들을 하나하나 복사한 객체를 반환하며,
그렇지 않은 클래스의 인스턴스에서 호출하면, ClassNotSupportedException을 던진다.

...



Deep copy Shallow Copy : https://jaehun2841.github.io/2019/01/13/java-object-copy/

reference : https://jaehun2841.github.io/2019/01/13/effective-java-item13/

 */
public class Item13Test {


}
