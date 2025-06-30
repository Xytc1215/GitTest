package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.util.List;

/**
 * Интерфейс для работы с данными пользователей.
 */
public interface UserDao {

    /**
     * Создаёт таблицу пользователей, если её ещё нет.
     */
    void createUsersTable();

    /**
     * Удаляет таблицу пользователей.
     */
    void dropUsersTable();

    /**
     * Добавляет пользователя в базу.
     */
    void saveUser(String name, String lastName, byte age);

    /**
     * Удаляет пользователя по ID.
     */
    void removeUserById(long id);

    /**
     * Получает список всех пользователей.
     */
    List<User> getAllUsers();

    /**
     * Очищает таблицу пользователей.
     */
    void cleanUsersTable();
}
