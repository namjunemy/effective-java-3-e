package chap04.item15;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Member {

    private String name;
    private int age;

    @Builder
    public Member(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
