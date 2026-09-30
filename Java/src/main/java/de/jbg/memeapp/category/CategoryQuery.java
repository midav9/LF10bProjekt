package de.jbg.memeapp.category;

import de.jbg.memeapp.SessionInitiator;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;

public class CategoryQuery extends SessionInitiator {   //DAO

    private Connection connection = getConnection();

    //Konstruktor
    public CategoryQuery() throws SQLException {
        super();
    }

    //getQuery
    public ArrayList<Category> execQuery(String getSqlQuery) throws SQLException {
        ArrayList<Category> resultArray = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(getSqlQuery);
        while (result.next()) {
            Category category = new Category();
            category.setCat_ID(result.getInt("cat_ID"));
            category.setCategory(result.getString("name"));

            //Fremdschlüssel bzw. sind auch NULL-Werte ok?, Attribute noch nicht definiert in Meme
            resultArray.add(category);
        }
        return resultArray;
    }

    //InputQuery
    InputStream inputStream;
    public void execInsert(String insertSqlQuery, String category) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(insertSqlQuery)) {
            stmt.setString(1, category);
            stmt.executeUpdate();
            System.out.println("Tag gespeichert");

        }
    }

}
