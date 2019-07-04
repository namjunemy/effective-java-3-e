package item08;

/*
아이템 8 - finalizer와 cleaner 사용을 피하라

cleaner(자바 8까지는 finalizer)는 안전망 역할이나 중요하지 않은 네이티브 자원 회수용으로만 사용하자.
물론 이런 경우라도 불확실성과 성능 저하에 주의해야 한다.

현재 Java 9 버전 부터는 Object 클래스에 대한 finalizer를 Deprecated 처리하였다.

"finalize 메소드의 오버라이딩을 자제해야 하는 이유."

모든 자바 클래스의 최상위 부모클래스인 java.lang.Object 클래스에는 finalize() 메소드가 존재하며,
Java API 에는 이 메소드는 '가비지 컬렉터가 레퍼런스를 잃은 클래스의 인스턴스를 가비지 컬렉션할 때 호출된다'
라고 기술하고 있습니다.

이 메소드는 객체인스턴스가 가비지 콜렉션에 의해 소멸되는 시점에
특정한 동작을 수행해야 할때도 요긴하게 사용할 수 있는 메소드입니다만
일반적인 경우 불필요하게 이 finalize() 메소드를 오버라이딩하는 것은 자제해야합니다.

이유는 'finalize 메소드에 의한 Collection 지연과 OOME(Out of Memory Exception)발생 가능성'때문입니다.
특정 Class에 finalize 메소드가 정의되어 있는 경우,
이 Class Type의 Object는 Garbage Collection 발생시 즉각적으로 Collection 되지 않습니다.
대신 Finalization Queue에 들어간 후 Finalizer에 의해 정리가 되는데요.
Finalizer는 Object의 finalize 메소드를 실행한 후 메모리 정리 작업을 수행하게됩니다.
만일 finalize 메소드를 수행하는데 오랜 시간이 걸린다면 그 만큼 객체가 오랫동안 메모리를 점유하게 되고
이로 인해 OOME가 발생할 확률이 높아집니다.
이런 이유로 finalize 메소드는 되도록 사용하지 말아야 합니다.

출처 - http://www.yunsobi.com/blog/entry/finalize-%EB%A9%94%EC%86%8C%EB%93%9C%EC%9D%98-%EC%98%A4%EB%B2%84%EB%9D%BC%EC%9D%B4%EB%94%A9%EC%9D%84-%EC%9E%90%EC%A0%9C%ED%95%B4%EC%95%BC%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0
 */

public class Item08Test {

}
