package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

import java.util.UUID;

public class UserdataUserDAOHibernate extends JpaService implements UserDataUserDAO {

    public UserdataUserDAOHibernate() {
        super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDB.USERDATA).createEntityManager());
    }

    @Override
    public int createUserInUserData(UserDataUserEntity user) {
        persist(user);
        return 0;
    }

    @Override
    public void deleteUserByIdInUserData(UUID userId) {
        UserDataUserEntity user = em.createQuery("select u from UserDataUserEntity u where u.id=:userId",
                        UserDataUserEntity.class)
                .setParameter("userId", userId)
                .getSingleResult();
        remove(user);
    }

    @Override
    public void deleteUserByUsernameInUserData(String username) {
        UserDataUserEntity user = em.createQuery("select u from UserDataUserEntity u where u.username=:username",
                        UserDataUserEntity.class)
                .setParameter("username", username)
                .getSingleResult();
        remove(user);
    }
}