package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static jm.task.core.jdbc.dao.SQL.*;


public class UserDaoHibernateImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoHibernateImpl.class);
    private final Hibernate hibernate;

    public UserDaoHibernateImpl(SessionFactory sessionFactory) {
        this.hibernate = new Hibernate(sessionFactory);
    }

    @Override
    public void createUsersTable() {
        hibernate.consume(s -> s.createNativeQuery(CREATE_TABLE).executeUpdate());
        logger.info("Таблица users создана");
    }

    @Override
    public void dropUsersTable() {
        hibernate.consume(s -> s.createNativeQuery(DROP_TABLE).executeUpdate());
        logger.info("Таблица users удалена");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        hibernate.consume(s -> {
            s.persist(new User(name, lastName, age));
            logger.info("Пользователь " + name + " был добавлен в БД");
        });
    }

    @Override
    public void removeUserById(long id) {
        hibernate.consume(s -> {
            User user = s.get(User.class, id);
            if (user != null) {
                s.remove(user);
                logger.info("Пользователь с id " + id + " был удален из таблицы users");
            }
        });
    }

    @Override
    public List<User> getAllUsers() {
        return hibernate.funct(s -> s.createQuery("from User").getResultList());
    }

    @Override
    public void cleanUsersTable() {
        hibernate.consume(s -> s.createQuery("delete from User").executeUpdate());
        logger.info("Таблица users очищена");
    }
}
