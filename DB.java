package com.db;

import java.net.UnknownServiceException;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public static void CreateTable() throws SQLException
    {
        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE IF NOT EXISTS 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'name' TEXT, 'login' TEXT, 'password' TEXT);");
    }

    public static boolean AddNewUser(String name, String login, String password) throws SQLException
    {
        if (isLoginExits(login) || isNameExits(name))
            return false;

        statmt.execute("INSERT INTO 'users' ('name', 'login', 'password') " +
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
            String  password = resSet.getString("password");
            User temp = new User();
            temp.id = id;
            temp.name = name;
            temp.login = login;
            temp.password = password;
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
            String  password = resSet.getString("password");
            user.id = id;
            user.name = name;
            user.login = login;
            user.password = password;
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
        resSet = statmt.executeQuery("SELECT * FROM users WHERE login='"+login+"' AND password='"+password+"'");

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
                "WHERE login='"+login+"' AND password='"+password+"'");

        return true;
    }

    public static boolean ReplacePassword(String login, String password, String new_password) throws SQLException {
        if (!isUserExists(login, password))
            return false;

        statmt.executeUpdate("UPDATE 'users' SET password='"+new_password+"' " +
                "WHERE login='"+login+"' AND password='"+password+"'");

        return true;
    }

    public static void Close() throws SQLException
    {
        conn.close();
        statmt.close();
    }
}
