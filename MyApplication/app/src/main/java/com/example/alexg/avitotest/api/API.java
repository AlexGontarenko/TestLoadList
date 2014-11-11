package com.example.alexg.avitotest.api;

import android.content.Context;

import com.example.alexg.avitotest.parser.UsersParser;
import com.example.alexg.avitotest.responce.ResultResponce;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by AlexG on 11.11.2014.
 */
public class API {

    private final static String urlData = "https://api.github.com/users";
    private final Context context;

    public API(Context context) {
        this.context = context;
    }

    public ResultResponce getUsers(){

        HttpsURLConnection connection = null;
        final UsersParser parser = new UsersParser();
        ResultResponce result = null;
        try {
            connection = (HttpsURLConnection) new URL(urlData).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(30000);
            connection.connect();
            int responce = connection.getResponseCode();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                if (parser != null) {
                    result = parser.parceData(connection.getInputStream());
                    connection.getInputStream().close();
                } else result = new ResultResponce(false,null);
            } else {
                result = new ResultResponce(false,null);
            }
        } catch (Exception e) {
            result = new ResultResponce(false,null);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return result;
    }
}
