package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.dao.impl.AuthUserDAOJdbc;
import guru.qa.niffler.db.dao.impl.AuthUserDAOSpringJdbc;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;

public class DBUserExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    private static final AuthUserDAO authUserDAO = new AuthUserDAOSpringJdbc();
    private static final UserDataUserDAO userDataUserDAO = new AuthUserDAOSpringJdbc();
    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DBUserExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
        if (annotation != null) {
            AuthUserEntity user = new AuthUserEntity();
            if (annotation.username().isEmpty() && annotation.password().isEmpty()) {
                user.setUsername(new Faker().name().username());
                user.setPassword(new Faker().internet().password());
            } else if (annotation.username().isEmpty()) {
                user.setUsername(new Faker().name().username());
                user.setPassword(annotation.password());
            } else if (annotation.password().isEmpty()) {
                user.setUsername(annotation.username());
                user.setPassword(new Faker().internet().password());
            } else {
                user.setUsername(annotation.username());
                user.setPassword(annotation.password());
            }
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
            authUserDAO.createUser(user);

            UserDataUserEntity userdataUser = new UserDataUserEntity();
            userdataUser.setUsername(user.getUsername());
            userdataUser.setCurrency(CurrencyValues.RUB);
            userDataUserDAO.createUserInUserData(userdataUser);

            context.getStore(NAMESPACE).put(context.getUniqueId(), user);
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        var user = context.getStore(NAMESPACE).get(context.getUniqueId(), AuthUserEntity.class);
        authUserDAO.deleteUserById(user.getId());
        userDataUserDAO.deleteUserByUsernameInUserData(user.getUsername());
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(AuthUserEntity.class);
    }

    @Override
    public AuthUserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(DBUserExtension.NAMESPACE).get(extensionContext.getUniqueId(),  AuthUserEntity.class);
    }
}
