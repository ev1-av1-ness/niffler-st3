package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.DBUser;
import org.junit.jupiter.api.Test;


import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DBUserExtensionWebTest extends BaseWebTest {

    @DBUser(username = "moon2", password = "12345")
    @Test
    void mainPageShouldBeVisibleAfterLogin(UserEntity userForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);
    }
}
