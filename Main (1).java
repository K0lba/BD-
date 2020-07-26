package com.db;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DB.Connect(); // Подключает базу данных
        DB.CreateTables(); // Создаёт таблицу, если она существует, то ничего не делает

        for(User user: DB.GetUsers()){ // Выводит спосок пользователей
            System.out.println("ID: " + user.id);
            System.out.println("name: " + user.name);
            System.out.println("login: " + user.login);
            System.out.println("password: " + user.passwordHash);
            System.out.println();
        }

        DB.AddNewUser("Name", "Login", "Password"); // Создает нового пользователя

        User some_user = DB.GetUserByLogin("Login"); // Получение пользователя по логину

        boolean res = DB.isUserExists("Login", "Password"); // Проверка на отсутсвие пользователя
        System.out.println("Проверка на отсутсвие пользователя - " + res);

        res = DB.isLoginExits("Login"); // Проверка на существование логина
        System.out.println("Проверка на существование логина - " + res);

        res = DB.isNameExits("Name"); // Проверка на существование имени
        System.out.println("Проверка на существование имени - " + res);

        res = DB.ReplaceName("Login", "New Name", "Password");
        System.out.println("Замена логина - " + res);

        res = DB.ReplacePassword("Login", "Password", "New password");
        System.out.println("Замена пароля - " + res);

        for(User user: DB.GetUsers()){ // Выводит спосок пользователей
            System.out.println("ID: " + user.id);
            System.out.println("name: " + user.name);
            System.out.println("login: " + user.login);
            System.out.println("password: " + user.passwordHash);
            System.out.println();
        }

        User user = DB.GetUserById(1);
        System.out.println("ID: " + user.id);
        System.out.println("name: " + user.name);
        System.out.println("login: " + user.login);
        System.out.println("password: " + user.passwordHash);
        System.out.println();


        for (String msg : DB.GetMessagesFromOneRoom(1, 2)){
            System.out.println(msg);
        }

        for (Map.Entry<Integer, ArrayList<String>> entry: DB.GetMessagesFromAllRooms(1).entrySet()){
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        DB.Close(); // Закрытие базы данных
    }
}

