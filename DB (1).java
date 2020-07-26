package com.db;

import java.net.UnknownServiceException;
import java.sql.*;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class DB {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    public static void Connect() throws ClassNotFoundException, SQLException
    {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
    }

    public static void CreateTables() throws SQLException
    {
        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE IF NOT EXISTS 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'name' TEXT, 'login' TEXT, 'passwordHash' TEXT);");
        statmt.execute("CREATE TABLE IF NOT EXISTS 'rooms' (room_id INTEGER, user_id INTEGER, message TEXT);");
    }

    public static boolean AddNewUser(String name, String login, String password) throws SQLException
    {
        if (isLoginExits(login) || isNameExits(name))
            return false;

        statmt.execute("INSERT INTO 'users' ('name', 'login', 'passwordHash') " +
                "VALUES ('"+name+"', '"+login+"', '"+password+"'); ");

        return true;
    }

    public static ArrayList<User> GetUsers() throws SQLException
    {
        resSet = statmt.executeQuery("SELECT * FROM users");
        ArrayList<User> users = new ArrayList<>();

        while(resSet.next())
        {
            int     id = resSet.getInt("id");
            String  login = resSet.getString("login");
            String  name = resSet.getString("name");
            String  password = resSet.getString("passwordHash");
            User temp = new User();
            temp.id = id;
            temp.name = name;
            temp.login = login;
            temp.passwordHash = password;
            users.add(temp);
        }

        return users;
    }

    public static User GetUserByLogin(String login_) throws SQLException{
        User user = new User();

        resSet = statmt.executeQuery("SELECT * FROM users WHERE login='"+login_+"'");

        while(resSet.next())
        {
            int     id = resSet.getInt("id");
            String  login = resSet.getString("login");
            String  name = resSet.getString("name");
            String  password = resSet.getString("passwordHash");
            user.id = id;
            user.name = name;
            user.login = login;
            user.passwordHash = password;
        }

        return user;
    }

    public static User GetUserById(int id) throws SQLException {
        User user = new User();

        resSet = statmt.executeQuery("SELECT * FROM users WHERE id='"+id+"'");

        while(resSet.next())
        {
            String  login = resSet.getString("login");
            String  name = resSet.getString("name");
            String  password = resSet.getString("passwordHash");
            user.id = id;
            user.name = name;
            user.login = login;
            user.passwordHash = password;
        }

        return user;
    }

    public static boolean isLoginExits(String login) throws SQLException {

        if (GetUserByLogin(login).login != ""){
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNameExits(String name) throws SQLException {

        User user = new User();

        resSet = statmt.executeQuery("SELECT * FROM users WHERE name='"+name+"'");

        while(resSet.next())
        {
            return true;
        }

        return false;

    }

    public static boolean isUserExists(String login, String password) throws SQLException {
        User user = new User();
        resSet = statmt.executeQuery("SELECT * FROM users WHERE login='"+login+"' AND passwordHash='"+password+"'");

        while(resSet.next())
        {
            return true;
        }
        return false;
    }

    public static boolean ReplaceName(String login, String new_name, String password) throws SQLException {
        if (!isUserExists(login, password))
            return false;

        statmt.executeUpdate("UPDATE 'users' SET name='"+new_name+"' " +
                "WHERE login='"+login+"' AND passwordHash='"+password+"'");

        return true;
    }

    public static boolean ReplacePassword(String login, String password, String new_password) throws SQLException {
        if (!isUserExists(login, password))
            return false;

        statmt.executeUpdate("UPDATE 'users' SET passwordHash='"+new_password+"' " +
                "WHERE login='"+login+"' AND passwordHash='"+password+"'");

        return true;
    }

    public static ArrayList<String> GetMessagesFromOneRoom(int user_id, int room_id) throws SQLException {
        ArrayList<String> messages = new ArrayList<>();

        resSet = statmt.executeQuery("SELECT message FROM rooms WHERE user_id="+user_id+" AND room_id="+room_id+"");
        while (resSet.next()){
            messages.add(resSet.getString("message"));
        }
        return messages;
    }

    public static HashMap<Integer, ArrayList<String>> GetMessagesFromAllRooms(int user_id) throws SQLException {
        HashMap<Integer, ArrayList<String>>  messages = new HashMap<>();
        ArrayList<Integer> rooms = new ArrayList<>();
        ArrayList<String> temp_messages;

        resSet = statmt.executeQuery("SELECT room_id FROM rooms WHERE user_id="+user_id+";");
        while (resSet.next()){
            rooms.add(resSet.getInt("room_id"));
        }
        for (int room_id : rooms){
            resSet = statmt.executeQuery("SELECT message FROM rooms WHERE room_id="+room_id+" and user_id="+user_id+";");
            temp_messages = new ArrayList<>();
            while (resSet.next()){
                temp_messages.add(resSet.getString("message"));
            }
            messages.put(room_id, temp_messages);
        }
        return messages;
    }

    public static void Close() throws SQLException
    {
        conn.close();
        statmt.close();
    }
}
