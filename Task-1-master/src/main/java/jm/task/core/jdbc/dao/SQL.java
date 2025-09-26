package jm.task.core.jdbc.dao;

public class SQL {
    public static final String CREATE_TABLE = """
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(128) NOT NULL,
                    lastName VARCHAR(128) NOT NULL,
                    age SMALLINT NOT NULL
                )
                """;

    public static final String DROP_TABLE = """
                DROP TABLE IF EXISTS users
                """;

    public static final String INSERT = """
                INSERT INTO users (name, lastName, age)
                values (?, ?, ?);
                """;

    public static final String REMOVE_BY_ID = """
                DELETE
                FROM users
                WHERE id = ?;
                """;

    public static final String GET_ALL = """
                SELECT * FROM users;
                """;

    public  static final String CLEAN_TABLE = """
                TRUNCATE TABLE users
                """;
}
