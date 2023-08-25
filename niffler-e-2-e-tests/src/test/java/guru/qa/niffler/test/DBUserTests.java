package guru.qa.niffler.test;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.AuthUserDAOJdbc;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.DBUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DBUserTests {

    @DBUser(
            username = "eliza",
            password = "12345"
    )
    @Test
    void dbUserCreateReadAndDeleteTest(UserEntity userEntity) {
        AuthUserDAO authUserDAO = new AuthUserDAOJdbc();
        UserEntity getUser = authUserDAO.getUserById(userEntity.getId());

        Assertions.assertEquals(userEntity.getUsername(), getUser.getUsername());
    }

    @DBUser(
            username = "eliza",
            password = "12345"
    )
    @Test
    void dbUserCreateUpdateAndReadTest(UserEntity userEntity) {
        AuthUserDAO authUserDAO = new AuthUserDAOJdbc();
        userEntity.setUsername("elizaveta");
        authUserDAO.updateUser(userEntity);

        UserEntity getUser = authUserDAO.getUserById(userEntity.getId());
        Assertions.assertEquals("elizaveta", getUser.getUsername());
    }
}
