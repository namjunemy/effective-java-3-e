package chap02.item09;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.assertj.core.api.Assertions.assertThat;

/*
아이템 9 - try-finally 보다는 try-with-resources 를 사용하라.

꼭 회수해야 하는 자원을 다룰 때는 try-finally 말고, try-with-resources 를 사용하자.
예외는 없다. 코드는 더 짧고 분명해지고, 만들어지는 예외 정보도 훨씬 유용하다.
try-finally로 작성하면 실용적이지 못할 만큼 코드가 지저분해지는 경우라도,
try-with-resources로는 정확하고 쉽게 자원을 회수할 수 있다.
 */
public class Item09Test {

    private static boolean copy() throws IOException {
        try (InputStream in = new FileInputStream("path");
             OutputStream out = new FileOutputStream("path")) {
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void tryWithResources() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("/test"))) {
            br.readLine();
        }
    }

    @Test
    public void tryWithTwoResources() throws IOException {
        assertThat(copy()).isFalse();
    }
}
