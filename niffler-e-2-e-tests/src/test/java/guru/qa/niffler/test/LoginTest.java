package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.impl.UserdataUserDAOHibernate;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.jupiter.annotation.Dao;
import guru.qa.niffler.jupiter.extension.DaoExtension;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.util.Arrays;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@ExtendWith(DaoExtension.class)
public class LoginTest extends BaseWebTest {

    private static final String defaultPassword = "12345";

    @Dao
    private AuthUserDAO authUserDAO;
    @Dao
    private UserDataUserDAO userDataUserDAO;

    private AuthUserEntity authUser;
    private UserDataUserEntity userdataUser;

    @BeforeEach
    void createUser() {
        authUser = new AuthUserEntity();
        authUser.setUsername("valentin_18");
        authUser.setPassword(defaultPassword);
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(Arrays.stream(Authority.values())
                .map(a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(a);
                    ae.setUser(authUser);
                    return ae;
                }).toList());
        authUserDAO.createUser(authUser);

        userdataUser = new UserDataUserEntity();
        userdataUser.setUsername("valentin_18");
        userdataUser.setCurrency(CurrencyValues.RUB);
        userDataUserDAO.createUserInUserData(userdataUser);
    }

    @AfterEach
    void deleteUser() {
        authUserDAO.deleteUserById(authUser.getId());
        userDataUserDAO.deleteUserByUsernameInUserData(userdataUser.getUsername());
    }

    @Test
    @AllureId("954")
    void mainPageShouldBeVisibleAfterLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(authUser.getUsername());
        $("input[name='password']").setValue(defaultPassword);
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);
    }

    @Test
    @AllureId("955")
    void wrongPassTest() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(authUser.getUsername());
        $("input[name='password']").setValue("wrongpassword");
        $("button[type='submit']").click();
        $(".form__error").should(text("Bad credentials"));
    }

    @Test
    @AllureId("956")
    void registerUser() {
        Selenide.open("http://127.0.0.1:9000/register");
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
        Selenide.open("http://127.0.0.1:9000/register");
        $("input[name='username']").setValue(new Faker().name().username());
        $("input[name='password']").setValue(defaultPassword);
        $("input[name='passwordSubmit']").setValue(new Faker().internet().password());
        $("button[type='submit']").click();
        $(".form__error").should(text("Passwords should be equal"));
    }

    @Test
    @AllureId("958")
    void registerUserWithTheSameName() {
        Selenide.open("http://127.0.0.1:9000/register");
        $("input[name='username']").setValue("barsik");
        $("input[name='password']").setValue(defaultPassword);
        $("input[name='passwordSubmit']").setValue(defaultPassword);
        $("button[type='submit']").click();
        $(".form__error").should(partialText("already exists"));
    }
}
