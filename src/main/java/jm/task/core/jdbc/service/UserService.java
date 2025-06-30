package jm.task.core.jdbc.service;

import jm.task.core.jdbc.model.User;

import java.util.List;

public interface UserService {

    /**
     * Создаёт таблицу пользователей, если она ещё не существует.
     */
    void createUsersTable();

    /**
     * Удаляет таблицу пользователей, если она существует.
     */
    void dropUsersTable();

    /**
     * Сохраняет пользователя с заданными параметрами в базу данных.
     */
    void saveUser(String name, String lastName, byte age);

    /**
     * Удаляет пользователя из базы по его идентификатору.
     */
    void removeUserById(long id);

    /**
     * Получает список всех пользователей из базы данных.
     */
    List<User> getAllUsers();

    /**
     * Очищает таблицу пользователей, удаляя все записи.
     */
    void cleanUsersTable();
}
