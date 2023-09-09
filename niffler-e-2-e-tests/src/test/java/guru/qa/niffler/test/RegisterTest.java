package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class RegisterTest {

    private static final String defaultPassword = "12345";

    @Test
    @AllureId("956")
    void registerUser() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='register']").click();
        $("input[name='username']").setValue(new Faker().name().username());
        $("input[name='password']").setValue(defaultPassword);
        $("input[name='passwordSubmit']").setValue(defaultPassword);
        $("button[type='submit']").click();
        $$(".form__paragraph").first().should(text("Congratulations! You've registered!"));
        $("a[href*='redirect']").should(interactable);
    }

    @Test
    @AllureId("957")
    void registerUserWithTheNotSamePass() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='register']").click();
        $("input[name='username']").setValue(new Faker().name().username());
        $("input[name='password']").setValue(defaultPassword);
        $("input[name='passwordSubmit']").setValue(new Faker().internet().password());
        $("button[type='submit']").click();
        $(".form__error").should(text("Passwords should be equal"));
    }

    @DBUser(
            username = "elizaveta",
            password = "12345"
    )
    @Test
    @AllureId("958")
    void registerUserWithTheSameName(AuthUserEntity userEntity) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='register']").click();
        $("input[name='username']").setValue(userEntity.getUsername());
        $("input[name='password']").setValue(defaultPassword);
        $("input[name='passwordSubmit']").setValue(defaultPassword);
        $("button[type='submit']").click();
        $(".form__error").should(partialText("already exists"));
    }
}
