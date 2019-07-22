package chap02.item07;

import static org.assertj.core.api.Assertions.assertThat;

import common.TestDescription;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.stream.IntStream;
import org.junit.Test;

/**
 * 아이템 7 : 다 쓴 객체 참조를 해제하라
 *
 * 메모리 누수는 겉으로 잘 드러나지 않아 수년 간 잠복하는 사례가 있다고 한다.
 * 이런 누수는 철저한 코드리뷰나 힙 프로파일링 도구를 통해 디버깅을 동원해야 발견할 수 있으므로,
 * 평소에 코드를 작성할 때 메모리 누수에 대한 부분을 신경 써주는 것이 중요하다.
 */
public class Item07Test {

    @Test
    @TestDescription("구현 Stack에서 메모리 누수 테스트")
    public void memoryLeak() {
        String name = "NAMJUNE KIM";
        Stack stack1 = new Stack();
        Stack stack2 = new Stack();
        Runtime runtime = Runtime.getRuntime();

        long start1 = runtime.totalMemory();
        IntStream.range(0, 2000).forEach((i) -> stack1.push(name + i));
        long end1 = runtime.totalMemory();

        long start2 = runtime.totalMemory();
        IntStream.range(0, 2000).forEach((i) -> stack2.push(name + i));
        IntStream.range(0, 500).forEach(value -> stack2.pop());
        long end2 = runtime.totalMemory();

        assertThat(stack1.size).isEqualTo(2000);
        assertThat(stack2.size).isEqualTo(1500);
        assertThat(end1 - start1).isEqualTo(end2 - start2);
    }

    class Stack {

        private Object[] elements;
        private int size = 0;
        private static final int DEFAULT_INITIAL_CAPACITY = 16;

        public Stack() {
            elements = new Object[DEFAULT_INITIAL_CAPACITY];
        }

        public void push(Object o) {
            ensureCapacity();
            elements[size++] = o;
        }

        public Object pop() {
            if (size == 0) {
                throw new EmptyStackException();
            }
            /*
            여기가 메모리 누수가 일어나는 지점이다.
            스택이 커졌다가 줄어들 떄 스택에서 꺼내진 객체들을 사용하지 않지만,
            스택이 그 객체들의 다 쓴 참조(obsolete reference)를 여전히 가지고 있기 때문에
            가비지 컬렉터가 회수하지 않는다.

            해결방법은 간단하다. 객체 참조 변수를 null로 초기화 하면 된다.
            그러나 모든 객체를 다 쓰자마자 일일이 null처리 하지는 않아도 된다. 필요 이상으로 지저분하게 만든다.
            객체 참조를 null 처리 하는 일은 예외적인 경우여야 한다.

            가비지 컬렉션의 소멸 대상이 되기 위해서는
            위에서 소개한 방법인
            1. 직접 할당을 통해 메모리를 해제하는 법
            2. Scope를 통해서 자동으로 할당 해제 하는법

            두 가지가 있다. 2번은 try-catch-finally, try-with-resources로 처리할 수 있다.
             */
            return elements[--size];

            // 해결
//            Object result = elements[--size];
//            elements[--size] = null;
//            return result;
        }

        private void ensureCapacity() {
            if (elements.length == size) {
                elements = Arrays.copyOf(elements, 2 * size + 1);
            }
        }
    }
}
