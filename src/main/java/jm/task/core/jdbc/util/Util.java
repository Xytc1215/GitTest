package jm.task.core.jdbc.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/gittest2";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "springcourse";

// JDBC
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Успешное подключение к базе");
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе");
            e.printStackTrace();
        }
        return connection;
    }

    // Hibernate
    private static SessionFactory sessionFactory;


    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                        .applySettings(getHibernateProperties())
                        .build();

                sessionFactory = new MetadataSources(registry)
                        .addAnnotatedClass(jm.task.core.jdbc.model.User.class)
                        .buildMetadata()
                        .buildSessionFactory();

            } catch (Exception e) {
                e.printStackTrace();
                if (sessionFactory != null) {
                    sessionFactory.close();
                }
            }
        }
        return sessionFactory;
    }

    // Настройки Hibernate в Properties
    private static Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.connection.url", URL);
        properties.put("hibernate.connection.username", USERNAME);
        properties.put("hibernate.connection.password", PASSWORD);
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "update"); // автоматически обновляет структуру таблиц
        return properties;
    }
}
