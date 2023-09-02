package guru.qa.niffler.test;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.impl.UserdataUserDAOHibernate;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DBUserTests {

    @DBUser(
            username = "elizav",
            password = "12345"
    )
    @Test
    void dbUserCreateReadAndDeleteTest(AuthUserEntity userEntity) {
        AuthUserDAO authUserDAO = new AuthUserDAOHibernate();
        AuthUserEntity getUser = authUserDAO.getUserById(userEntity.getId());

        Assertions.assertEquals(userEntity.getUsername(), getUser.getUsername());
    }

    @DBUser(
            username = "eliza",
            password = "12345"
    )
    @Test
    void dbUserCreateUpdateAndReadTest(AuthUserEntity userEntity) {
        AuthUserDAO authUserDAO = new AuthUserDAOHibernate();
        userEntity.setEnabled(false);
        authUserDAO.updateUser(userEntity);

        AuthUserEntity getUser = authUserDAO.getUserById(userEntity.getId());
        Assertions.assertFalse(getUser.getEnabled());
    }
}
