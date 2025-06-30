package jm.task.core.jdbc.util;

import jm.task.core.jdbc.exception.SessionFactoryCreationException;
import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jm.task.core.jdbc.exception.DatabaseConnectionException;
import static java.sql.DriverManager.getConnection; // Статический импорт метода

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Класс конфигурации
 * - JDBC Connection
 * - Hibernate SessionFactory
 */
public class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    private static final String URL = "jdbc:mysql://localhost:3306/gittest2?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "springcourse";

    private static SessionFactory sessionFactory;

    /**
     * Получение JDBC Connection для работы через JDBC.
     */
    public static Connection createJdbcConnection() {
        try {
            return getConnection(URL, USERNAME, PASSWORD);  // Можно статический импорт DriverManager.getConnection, чтобы здесь писать просто getConnection (готово)
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Ошибка при подключении к базе данных", e); // в следующих модулях лучше сделать кастомные эксепшены, и ими кидаться
        }
    }
// Перепиши: Проверяет наличие SessionFactory, при отсутствии — инициализирует (готово)

    /**
     * Получает синглтон Hibernate SessionFactory.
     * Если объект ещё не создан — настраивает его.
     */
    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();

            Properties settings = new Properties();
            settings.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            settings.put("hibernate.connection.url", URL);
            settings.put("hibernate.connection.username", USERNAME);
            settings.put("hibernate.connection.password", PASSWORD);
            settings.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            settings.put("hibernate.hbm2ddl.auto", "none");
            settings.put("hibernate.show_sql", "true");

            configuration.setProperties(settings);
            configuration.addAnnotatedClass(User.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            logger.info("Hibernate SessionFactory created successfully");

            return sessionFactory;
        } catch (Exception e) {
            logger.error("Error creating SessionFactory", e);
            throw new SessionFactoryCreationException("Error creating SessionFactory", e);
        }
    }
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }
}

