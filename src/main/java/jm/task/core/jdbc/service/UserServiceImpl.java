package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.model.User;


import java.util.List;

/**
 * Сервис для управления пользователями.
 * Выполняет бизнес-логику и делегирует работу с базой данных слою DAO.
 */

// джава док перенеси в интерфейс

/**
 * Реализация бизнес-логики для управления пользователями.
 * Делегирует работу с базой данных объекту DAO.
 */

// для чего здесь две пусты строки?
public class UserServiceImpl implements UserService {


    private final UserDao userDao;

    /**
     * Конструктор с внедрением зависимости. // если в этом классе будешь добавлять ламбок, то можно убрать конструктор
     */
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Создаёт таблицу пользователей.
     */
    @Override
    public void createUsersTable() {
        userDao.createUsersTable();
    }

    /**
     * Удаляет таблицу пользователей.
     */
    @Override
    public void dropUsersTable() {
        userDao.dropUsersTable();
    }

    /**
     * Сохраняет пользователя в базу данных.
     */
    @Override
    public void saveUser(String name, String lastName, byte age) {
        userDao.saveUser(name, lastName, age);
    }

    /**
     * Удаляет пользователя по ID.
     */
    @Override
    public void removeUserById(long id) {
        userDao.removeUserById(id);
    }

    /**
     * Получает всех пользователей из базы.
     */
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    /**
     * Удаляет все записи из таблицы.
     */
    @Override
    public void cleanUsersTable() {
        userDao.cleanUsersTable();
    }
}
