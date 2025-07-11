package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Класс, для работы с таблицей пользователей с помощью Hibernate.
 * Выполняет создание/удаление таблицы, добавление/удаление пользователей,
 * получение всех пользователей и очистка таблицы.
 */

// джава док в интерфейс. Пример рефакторинга смотри в соседнем классе
public class UserDaoHibernateImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoHibernateImpl.class);

    public UserDaoHibernateImpl() {
    }

    @FunctionalInterface
    private interface TransactionOperation<T> {
        T execute(Session session);
    }

    private <T> T runInTransaction(TransactionOperation<T> operation) {
        Transaction transaction = null;
        T result = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = operation.execute(session);
            if (transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            logger.error("Error while executing transaction", e);
        }
        return result;
    }

    @Override
    public void createUsersTable() {
        runInTransaction(session -> {
            String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(50),
                    lastName VARCHAR(50),
                    age TINYINT
                )
            """;
            session.createSQLQuery(sql).executeUpdate();
            logger.info("Таблица 'users' успешно создана (если не существовала)");
            return null;
        });
    }

    @Override
    public void dropUsersTable() {
        runInTransaction(session -> {
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            logger.info("Таблица 'users' удалена (если существовала)");
            return null;
        });
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        runInTransaction(session -> {
            User user = new User();
            user.setName(name);
            user.setLastName(lastName);
            user.setAge(age);
            session.save(user);
            logger.info("Пользователь с именем '{}' добавлен в базу", name);
            return null;
        });
    }

    @Override
    public void removeUserById(long id) {
        runInTransaction(session -> {
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                logger.info("Пользователь с id {} удалён", id);
            } else {
                logger.warn("Пользователь с id {} не найден", id);
            }
            return null;
        });
    }

    @Override
    public List<User> getAllUsers() {
        return runInTransaction(session -> {
            List<User> users = session.createQuery("FROM User", User.class).list();
            logger.info("Получено {} пользователей из базы", users.size());
            return users;
        });
    }

    @Override
    public void cleanUsersTable() {
        runInTransaction(session -> {
            int count = session.createQuery("DELETE FROM User").executeUpdate();
            logger.info("Удалено {} пользователей из таблицы", count);
            return null;
        });
    }
}
