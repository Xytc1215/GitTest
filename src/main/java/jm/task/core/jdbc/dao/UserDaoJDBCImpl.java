package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация DAO для работы с таблицей пользователей через JDBC.
 * Содержит методы создания/удаления таблицы, добавления/удаления пользователей,
 * получения всех пользователей и очистки таблицы.
 */
public class UserDaoJDBCImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoJDBCImpl.class);

    public UserDaoJDBCImpl() {
    }

    /**
     * Создаёт таблицу пользователей, если её ещё нет.
     */
    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(50), " +
                "lastName VARCHAR(50), " +
                "age TINYINT)";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            logger.info("Таблица users создана или уже существует.");
        } catch (SQLException e) {
            logger.error("Ошибка при создании таблицы users", e);
        }
    }

    /**
     * Удаляет таблицу пользователей, если она существует.
     */
    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            logger.info("Таблица users удалена, если существовала.");
        } catch (SQLException e) {
            logger.error("Ошибка при удалении таблицы users", e);
        }
    }

    /**
     * Сохраняет нового пользователя в таблицу.
     */
    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            logger.info("Пользователь с именем {} добавлен в базу.", name);
        } catch (SQLException e) {
            logger.error("Ошибка при сохранении пользователя", e);
        }
    }

    /**
     * Удаляет пользователя по его ID.
     */
    @Override
    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Пользователь с id={} удалён.", id);
            } else {
                logger.warn("Пользователь с id={} не найден для удаления.", id);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при удалении пользователя с id=" + id, e);
        }
    }

    /**
     * Получает список всех пользователей из таблицы.
     */
    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
            }
            logger.info("Получен список всех пользователей. Количество: {}", userList.size());
        } catch (SQLException e) {
            logger.error("Ошибка при получении списка пользователей", e);
        }
        return userList;
    }

    /**
     * Очищает таблицу пользователей (удаляет все записи).
     */
    @Override
    public void cleanUsersTable() {
        String sql = "DELETE FROM users";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            logger.info("Таблица users очищена.");
        } catch (SQLException e) {
            logger.error("Ошибка при очистке таблицы users", e);
        }
    }
}
