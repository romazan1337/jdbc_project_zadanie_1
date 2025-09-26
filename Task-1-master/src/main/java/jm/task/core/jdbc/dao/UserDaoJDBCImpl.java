package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.dao.SQL.*;


public class UserDaoJDBCImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoJDBCImpl.class);
    private final JDBC jdbc;

    public UserDaoJDBCImpl() {
        this.jdbc = new JDBC();
    }

    public void createUsersTable() {
        jdbc.execute(CREATE_TABLE);
        logger.info("Таблица users создана");
    }

    public void dropUsersTable() {
        jdbc.execute(DROP_TABLE);
        logger.info("Таблица users удалена");
    }

    public void saveUser(String name, String lastName, byte age) {
        jdbc.executeUpdate(INSERT, preparedStatement -> {
            try {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setByte(3, age);
            } catch (SQLException e) {
                throw new RuntimeException("Ошибка вставки пользователя" + name + e);
            }
            logger.info("Пользователь " + name + " был добавлен в БД");
        });
    }

    public void removeUserById(long id) {
        jdbc.executeUpdate(REMOVE_BY_ID, preparedStatement -> {
            try {
                preparedStatement.setLong(1, id);
            } catch (SQLException e) {
                throw new RuntimeException("Ошибка удаления пользователя по id" + e);
            }
            logger.info("Пользователь с id " + id + " был удален из таблицы users");
        });
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        return jdbc.executeQuery(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    users.add(new User(
                            resultSet.getString("name"),
                            resultSet.getString("lastName"),
                            resultSet.getByte("age")
                    ));
                }
            } catch (SQLException e) {
                throw new RuntimeException("Ошибка получения всех пользователей" + e);
            }
            return users;
        });
    }

    public void cleanUsersTable() {
        jdbc.execute(CLEAN_TABLE);
        logger.info("Таблица users очищена");
    }
}
