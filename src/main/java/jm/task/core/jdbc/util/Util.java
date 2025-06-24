package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/gittest2";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "springcourse";

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
}


