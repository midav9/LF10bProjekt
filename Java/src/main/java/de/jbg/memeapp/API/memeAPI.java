package de.jbg.memeapp.API;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.jbg.memeapp.Memes.Meme;
import de.jbg.memeapp.Memes.MemeQuery;
import org.json.JSONObject;
import org.mariadb.jdbc.MariaDbBlob;

public class memeAPI extends generalAPI implements HttpHandler {

    MemeQuery memeStatement;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String httpMethod = exchange.getRequestMethod(); //hier da sonst NullPointerException

        switch (httpMethod) {
            case "GET":
                //getBlobOfOneMeme
                Blob response = new MariaDbBlob();   //API-Response
                String getSqlQuery = "Select * FROM memes WHERE meme_ID=" + getRequestID(exchange);
                try {
                    MemeQuery memeQuery = new MemeQuery();
                    ArrayList<Meme> memes = memeQuery.execGetQuery(getSqlQuery);
                    for (Meme meme : memes) {
                        response = meme.getPic();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                try {
                    exchange.sendResponseHeaders(200, response.length()); //length needed for image reader in UI
                } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }
                outputStream = exchange.getResponseBody();
                try {
                    outputStream.write(response.getBinaryStream().readAllBytes());
                } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }
                outputStream.close();
                break;

            /* for abstract method call
            case "POST":
                inputStream = exchange.getRequestBody();
                //maybe make input dynamic based on the file (https://github.com/haraldk/TwelveMonkeys)
                String setSqlQuery = "INSERT INTO memes (pic, date, height, length, size, category, tag) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try {
                    memeStatement = new MemeQuery();
                    memeStatement.execInsertWithBlob(setSqlQuery,
                            inputStream,
                            Date.valueOf(LocalDate.now()),
                            100, 100, 32, 1, 1
                    );
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                inputStream.close();
                sendSome200Response(exchange); //just as good behaviour
                break;

             */

            case "POST":
                // JSON Body einlesen
                inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                JSONObject obj = new JSONObject(body);

                int vHeight = obj.getInt("height");
                int vLength = obj.getInt("length");
                int vSize = obj.getInt("size");
                int vCategory = obj.getInt("category");
                int vTag = obj.getInt("tag");

                // Bild zurück in ByteArray
                byte[] imageBytes = Base64.getDecoder().decode(obj.getString("image"));

                // Beispiel: Insert in DB
                String setSqlQuery = "INSERT INTO memes (pic, date, height, length, size, category, tag) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try {
                    memeStatement = new MemeQuery();
                    memeStatement.execInsertWithBlob(setSqlQuery,
                            new java.io.ByteArrayInputStream(imageBytes),
                            Date.valueOf(LocalDate.now()),
                            vHeight,
                            vLength,
                            vSize,
                            vCategory,
                            vTag
                    );
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                // Antwort zurück
                String response2 = "Meme gespeichert mit Größe: " + imageBytes.length + " Bytes";
                exchange.sendResponseHeaders(200, response2.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response2.getBytes());
                os.close();
                break;

            case "PUT":
                inputStream = exchange.getRequestBody();
                String inputString = new String(inputStream.readAllBytes());
                String updateSqlQuery = "UPDATE memes SET DATE = '" + inputString + "' WHERE meme_ID = " + getRequestID(exchange);
                try {
                    memeStatement = new MemeQuery();
                    memeStatement.execSomeQuery(updateSqlQuery);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                inputStream.close();
                sendSome200Response(exchange); //just as good behaviour
                break;

            case "DELETE":
                String deleteSqlQuery = "DELETE FROM memes WHERE meme_ID = " + getRequestID(exchange);
                try {
                    memeStatement = new MemeQuery();
                    memeStatement.execSomeQuery(deleteSqlQuery);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                sendSome200Response(exchange); //just as good behaviour
                break;

        }

    }
}
