package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.impl.UserdataUserDAOHibernate;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.jupiter.annotation.Dao;
import guru.qa.niffler.jupiter.extension.DaoExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

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
        authUser.setUsername("valentin_12");
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
        userdataUser.setUsername("valentin_12");
        userdataUser.setCurrency(CurrencyValues.RUB);
        userDataUserDAO.createUserInUserData(userdataUser);
    }

    @AfterEach
    void deleteUser() {
        authUserDAO.deleteUserById(authUser.getId());
        userDataUserDAO.deleteUserByUsernameInUserData(userdataUser.getUsername());
    }

    @Test
    void mainPageShouldBeVisibleAfterLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(authUser.getUsername());
        $("input[name='password']").setValue(defaultPassword);
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);
    }
}
