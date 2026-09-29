package de.jbg.memeapp;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.Optional;

public abstract class SessionInitiator {

    //Variables
    private Connection connection;
    private String db = "memedb";
    private Integer currPic;
    private String sqlQuery;
    private String column;

    //Konstruktor
    public SessionInitiator() throws SQLException {

        //Session Setup
        Dotenv dotenv = Dotenv.load();
        String url = "jdbc:mariadb://localhost:3306/"+db;
        String user = Optional.ofNullable(dotenv.get("API_USER_DB")).orElseThrow(() -> new IllegalStateException("API_USER_DB env var is not defined"));
        String password = Optional.ofNullable(dotenv.get("API_KEY_DB")).orElseThrow(() -> new IllegalStateException("API_KEY_DB env var is not defined"));

        //establish Session
        try {
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection Established successfully");
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    //getter
    public Connection getConnection() {
        return this.connection;
    }

}
