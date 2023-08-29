package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.dao.impl.AuthUserDAOJdbc;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;

public class DBUserExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    private static final AuthUserDAO authUserDAO = new AuthUserDAOJdbc();
    private static final UserDataUserDAO userDataUserDAO = new AuthUserDAOJdbc();
    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DBUserExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
        if (annotation != null) {
            AuthUserEntity user = new AuthUserEntity();
            user.setUsername(annotation.username());
            user.setPassword(annotation.password());
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setAuthorities(Arrays.stream(Authority.values())
                    .map(authority -> {
                        var ae = new AuthorityEntity();
                        ae.setAuthority(authority);
                        return ae;
                    }).toList()
            );
            context.getStore(NAMESPACE).put("user", user);
            authUserDAO.createUser(user);

            UserDataUserEntity userdataUser = new UserDataUserEntity();
            userdataUser.setUsername("valentin_6");
            userdataUser.setCurrency(CurrencyValues.RUB);
            userDataUserDAO.createUserInUserData(userdataUser);
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        var user = context.getStore(NAMESPACE).get("user", AuthUserEntity.class);
        userDataUserDAO.deleteUserByUsernameInUserData(user.getUsername());
        authUserDAO.deleteUser(user);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(AuthUserEntity.class);
    }

    @Override
    public AuthUserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(DBUserExtension.NAMESPACE).get("user", AuthUserEntity.class);
    }
}
