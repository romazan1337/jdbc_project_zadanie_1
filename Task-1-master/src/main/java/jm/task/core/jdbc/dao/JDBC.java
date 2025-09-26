package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.util.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;
import java.util.function.Function;

public class JDBC {
    private static final Logger logger = LoggerFactory.getLogger(JDBC.class);

    public void execute(Consumer<Connection> consumer) {
        this.executeQuery(connection -> {
            consumer.accept(connection);
            return null;
        });
    }

    public void execute(String sql) {
        execute(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public <R> R executeQuery(Function<Connection, R> function) {
        try (Connection connection = ConnectionManager.open()) {
            connection.setAutoCommit(false);
            try {
                R result = function.apply(connection);
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeUpdate(String sql, Consumer<PreparedStatement> parameterSetter) {
        this.execute(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                parameterSetter.accept(preparedStatement);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
