package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Vitalii", "Aleshin", (byte) 25);
        System.out.println("User с именем – Vitalii добавлен в базу данных");

        userService.saveUser("Petrov", "Ivanov", (byte) 30);
        System.out.println("User с именем – Petrov добавлен в базу данных");

        userService.saveUser("Anna", "Smirnova", (byte) 22);
        System.out.println("User с именем – Anna добавлен в базу данных");

        userService.saveUser("Olga", "Sidorova", (byte) 28);
        System.out.println("User с именем – Olga добавлен в базу данных");

        List<User> users = userService.getAllUsers();

        for (User user : users) {
            System.out.println(user);
        }

        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}