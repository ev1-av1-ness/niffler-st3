package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.DBUser;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DBUserExtensionWebTest extends BaseWebTest {
    @BeforeEach
    void doLogin(UserEntity userForTest) {
        System.out.println(userForTest);
    }

    @Test
    void mainPageShouldBeVisibleAfterLogin(@DBUser(username = "", password = "") UserEntity userForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);
    }
}