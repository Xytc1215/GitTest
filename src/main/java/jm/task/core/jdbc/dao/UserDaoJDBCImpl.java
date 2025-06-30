package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getConnection;

/**
 * Что улучшилось:
 *         - Централизованная обработка ошибок
 *         - Метод executeStatement() - Дублирование statement.execute...
 *         - Всегда выбрасывается RuntimeException - Неявное поведение при ошибке
 *         - Логирование разрозненное	С единообразным форматом. На АНГЛИЙСКОМ, часто настроено так, что логи на русском будут отображаться каракулями.
 *         - Все выражения вынесены в константы
 *
 */


/**
 * Класс, для управления пользователями через JDBC.
 */
public class UserDaoJDBCImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoJDBCImpl.class);

    private static final String CREATE_USERS_TABLE_SQL = "CREATE TABLE IF NOT EXISTS users (" +
            "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
            "name VARCHAR(50), " +
            "lastName VARCHAR(50), " +
            "age TINYINT)";

    private static final String DROP_USERS_TABLE_SQL = "DROP TABLE IF EXISTS users";
    private static final String INSERT_USER_SQL = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
    private static final String DELETE_USER_BY_ID_SQL = "DELETE FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS_SQL = "SELECT * FROM users";
    private static final String CLEAN_USERS_TABLE_SQL = "DELETE FROM users";

    public UserDaoJDBCImpl() {
    }

    @Override
    public void createUsersTable() {
        executeStatement(CREATE_USERS_TABLE_SQL, "Failed to create users table");
    }

    @Override
    public void dropUsersTable() {
        executeStatement(DROP_USERS_TABLE_SQL, "Failed to drop users table");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();

            logger.info("User with name {} added to the database.", name);

        } catch (SQLException e) {
            handleSQLException("Failed to save user " + name, e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                logger.info("User with id={} has been deleted.", id);
            } else {
                logger.warn("User with id={} not found for deletion.", id);
            }

        } catch (SQLException e) {
            handleSQLException("Failed to delete user with id=" + id, e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS_SQL)) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
            }

            logger.info("Retrieved {} users from the database.", userList.size());

        } catch (SQLException e) {
            handleSQLException("Failed to retrieve users", e);
        }

        return userList;
    }

    @Override
    public void cleanUsersTable() {
        executeStatement(CLEAN_USERS_TABLE_SQL, "Failed to clean users table");
    }

    /**
     * Выполняет простой SQL-запрос без параметров.
     */
    private void executeStatement(String sql, String errorMessage) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql);
            logger.info("{} executed successfully.", sql);

        } catch (SQLException e) {
            handleSQLException(errorMessage, e);
        }
    }

    /**
     * Централизованная обработка SQL-исключений.
     */
    private void handleSQLException(String message, SQLException e) {
        logger.error(message, e);
        throw new RuntimeException(message, e);
    }
}
