package item02;

import common.TestDescription;
import org.junit.Test;

/**
 * 아이템 2 : 생성자에 매개변수가 많다면 빌더를 고려하라
 *
 * 생성자나 정적 팩터리가 처리해야 할 매개변수가 많다면 빌더 패턴을 선택하는게 더 낫다.
 * 매개변수 중 다수가 아니거나 같은 타입이면 특히 더 그렇다.
 * 빌더는 점층적 생성자보다 클라이언트 코드를 읽고 쓰기가 훨씬 간결하고, setter 방식(자바빈즈) 보다 안전하다.
 *
 * 빌더 패턴의 장점
 * - 각 인자가 어떤 의미인지 알기 쉽다.
 * - setter 메소드가 없으므로 변경 불가능 객체를 만들 수 있다.
 * - 한 번에 객체를 생성하므로 객체 일관성이 깨지지 않는다.
 * - build() 함수가 잘못된 값이 입력되었는지 검증하게 할 수도 있다.
 */
public class item02Test {

    @Test
    @TestDescription("선택적 매개변수가 많을 경우 적절하게 대응하기 어렵다.")
    public void singleConstructor() {
        NutritionFactsOfSingleConstructor nutritionFacts =
            new NutritionFactsOfSingleConstructor(200, 5, 500, 200, 100, 100);
    }

    @Test
    @TestDescription("위의 문제를 해결하려는 노력이 점층적 생성자 패턴이다.")
    public void telescopingConstructor() {
        NutritionFactsOfTelescopingConstructor nutritionFacts;

        nutritionFacts = new NutritionFactsOfTelescopingConstructor(200, 5);
        nutritionFacts = new NutritionFactsOfTelescopingConstructor(200, 5, 500);
        nutritionFacts = new NutritionFactsOfTelescopingConstructor(200, 5, 500, 200, 100, 100);
    }

    @Test
    @TestDescription("자바 빈즈 패턴. 객체가 완전히 생성되기 전까지는 일관성이 무너진 상태에 노이게 된다.")
    public void javaBeans() {
        NutritionFactsOfJavaBeans nutritionFacts = new NutritionFactsOfJavaBeans();
        nutritionFacts.setServingSize(200);
        nutritionFacts.setServings(5);
        nutritionFacts.setCalories(500);
        nutritionFacts.setFat(200);
        nutritionFacts.setSodium(100);
        nutritionFacts.setCarbohydrate(100);
    }

    @Test
    @TestDescription("빌더 패턴을 사용하면 아래와 같이 사용할 수 있다.")
    public void builder() {
        NutritionFactsOfBuilder nutritionFacts = new NutritionFactsOfBuilder
            .Builder(200, 5)
            .calories(500)
            .fat(100)
            .sodium(100)
            .carbohydrate(100)
            .build();

        nutritionFacts = new NutritionFactsOfBuilder
            .Builder(200, 5)
            .calories(500)
            .fat(100)
            .build();
    }
}
