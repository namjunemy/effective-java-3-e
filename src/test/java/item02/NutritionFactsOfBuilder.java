package item02;

public class NutritionFactsOfBuilder {

    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public static class Builder {

        // 필수 인자
        private final int servingSize;
        private final int servings;

        // 선택적 인자는 기본값으로 초기화
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;    //필수
            this.servings = servings;          //필수
        }

        public Builder calories(int calories) {
            this.calories = calories;
            return this;
        }

        public Builder fat(int fat) {
            this.fat = fat;
            return this;

        }

        public Builder sodium(int sodium) {
            this.sodium = sodium;
            return this;
        }

        public Builder carbohydrate(int carbohydrate) {
            this.carbohydrate = carbohydrate;
            return this;
        }

        public NutritionFactsOfBuilder build() {
            return new NutritionFactsOfBuilder(this);
        }
    }

    private NutritionFactsOfBuilder(Builder builder) {
        this.servingSize = builder.servingSize;    //필수
        this.servings = builder.servings;          //필수
        this.calories = builder.calories;
        this.fat = builder.fat;
        this.sodium = builder.sodium;
        this.carbohydrate = builder.carbohydrate;
    }
}
