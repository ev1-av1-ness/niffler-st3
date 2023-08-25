package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.DBUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DBUserExtensionBeforeTestGetUserWebTest extends BaseWebTest {

    private UserEntity userForTest;

    @BeforeEach
    void beforeEach(UserEntity user) {
        this.userForTest = user;
        System.out.println(userForTest.getUsername());
    }

    @DBUser(username = "moon2", password = "12345")
    @Test
    void dbUserExtensionBeforeTestGetUserWebTest1(UserEntity user) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);
    }

    @DBUser(username = "moon4", password = "12345")
    @Test
    void dbUserExtensionBeforeTestGetUserWebTest2(UserEntity user) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);
    }
}
