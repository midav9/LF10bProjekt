//https://github.com/danvega/todos-http-client/blob/main/src/main/java/todo/TodoClient.java

package de.jbg.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Blob;
import java.util.Base64;


import static de.jbg.ui.UI.vHeight;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
//import de.jbg.ui.Execption;


public class memeClient {


    private final static String BASE_URL = "http://localhost:8000/api/memes";
    private final HttpClient client;
    public memeClient() {
        client = HttpClient.newHttpClient();
    }

    //GET-Methods
    public ImageIcon findById(int memeID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/" + memeID)).GET().build();
        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        /*
        if(response.statusCode() == 404) {
            throw new memeNotFoundException("memeID.Meme not found");
        }
         */
        System.out.println("Content-Type: " + response.headers().firstValue("Content-Type").orElse("unknown"));
        byte[] responseArray = response.body();
        InputStream inputImage = new ByteArrayInputStream(responseArray);
        BufferedImage buffImage = ImageIO.read(inputImage);
        ImageIcon imgIcon = new ImageIcon(buffImage);

        return imgIcon;
    }


    //POST-Methods
//    public HttpResponse<String> postImage(String filePath) throws IOException, InterruptedException {   //can be exchanged to datatype Path instead of String
//        byte[] testPicBytes;
//            try {
//                testPicBytes = Files.readAllBytes(Path.of(filePath));
//            } catch (IOException exception) {
//                throw new RuntimeException(exception);
//            }
//            String deineMudda = new String(testPicBytes, StandardCharsets.UTF_8);
////        HttpRequest request = HttpRequest.newBuilder()
////                .uri(URI.create(BASE_URL))
////                .POST(HttpRequest.BodyPublishers.ofByteArray(testPicBytes))
////                .build();
//        String requestBody = "param1=" + URLEncoder.encode(UI.vHeight) + "&param2=" + URLEncoder.encode(UI.vLength) + "&param3=" + URLEncoder.encode(UI.vSize) + "&param4=" + URLEncoder.encode(UI.vCategory) + "&param5=" + URLEncoder.encode(UI.vTag) + "&param6=" + URLEncoder.encode(deineMudda);
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(BASE_URL))
//                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                .build();
//        System.out.println(requestBody);
//        return client.send(request, HttpResponse.BodyHandlers.ofString());
//    }

    public HttpResponse<String> postImage(String filePath,
                                          int vHeight,
                                          int vLength,
                                          int vSize,
                                          int vCategory,
                                          int vTag) throws IOException, InterruptedException {

        byte[] imageBytes = Files.readAllBytes(Path.of(filePath));
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        String json = "{"
                + "\"height\":" + vHeight + ","
                + "\"length\":" + vLength + ","
                + "\"size\":" + vSize + ","
                + "\"category\":" + vCategory + ","
                + "\"tag\":" + vTag + ","
                + "\"image\":\"" + base64Image + "\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    public HttpResponse<String> updateDateToToday(String memeID) throws IOException, InterruptedException {
        String currentDay = String.valueOf(Date.valueOf(LocalDate.now()));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + memeID))
                .PUT(HttpRequest.BodyPublishers.ofString(currentDay))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> deleteMeme(Integer memeID) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + memeID))
                .DELETE()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /*
    //this works, but is not useful at the moment
    public String getAll() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    */

}