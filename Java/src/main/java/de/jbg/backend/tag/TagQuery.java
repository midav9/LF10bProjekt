package de.jbg.memeapp.tag;

import de.jbg.memeapp.SessionInitiator;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;

public class TagQuery extends SessionInitiator {    //DAO

    private Connection connection = getConnection();

    //Konstruktor
    public TagQuery() throws SQLException {
        super();
    }

    //getQuery
    public ArrayList<Tag> execGetQuery(String getSqlQuery) throws SQLException {
        ArrayList<Tag> resultArray = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(getSqlQuery);
        while (result.next()) {
            Tag tag = new Tag();
            tag.setTag_ID(result.getInt("tag_ID"));
            tag.setName(result.getString("name"));

            //Fremdschlüssel bzw. sind auch NULL-Werte ok?, Attribute noch nicht definiert in Meme
            resultArray.add(tag);
        }
        return resultArray;
    }

    //InsertQuery
    InputStream inputStream;
    public void execInsert(String insertSqlQuery, String name) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(insertSqlQuery)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println("Tag gespeichert");

        }
    }

    //Update & Delete Query
    public void execSomeQuery(String someSqlQuery) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(someSqlQuery)) {
            stmt.executeUpdate();
            System.out.println("Aktion ausgeführt");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
