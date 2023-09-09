package guru.qa.niffler.test;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

public class DBUserTests {

    @DBUser(
            username = "elizaveta1",
            password = "12345"
    )
    @AllureId("342")
    @Test
    void dbUserCreateReadAndDeleteTest(AuthUserEntity userEntity) {
        AuthUserDAO authUserDAO = new AuthUserDAOHibernate();
        AuthUserEntity getUser = authUserDAO.getUserById(userEntity.getId());

        Assertions.assertEquals(userEntity.getUsername(), getUser.getUsername());
    }

    @DBUser(
            username = "elizium",
            password = "12345"
    )
    @AllureId("142")
    @Test
    void dbUserCreateUpdateAndReadTest(AuthUserEntity userEntity) {
        AuthUserDAO authUserDAO = new AuthUserDAOHibernate();
        userEntity.setEnabled(false);
        authUserDAO.updateUser(userEntity);

        AuthUserEntity getUser = authUserDAO.getUserById(userEntity.getId());
        Assertions.assertFalse(getUser.getEnabled());
    }
}
