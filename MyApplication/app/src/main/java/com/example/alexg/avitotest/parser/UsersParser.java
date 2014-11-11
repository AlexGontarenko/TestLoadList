package com.example.alexg.avitotest.parser;

import android.text.TextUtils;

import com.example.alexg.avitotest.enity.User;
import com.example.alexg.avitotest.responce.ResultResponce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by AlexG on 11.11.2014.
 */
public class UsersParser {

    public ResultResponce parceData(InputStream is) {
        ArrayList<User> userList = null;
        String response = null;
        JSONObject objectJSON=null,object=null,imgObject;
        JSONArray arrayJSON = null;
        int i,size;
        User user;
        try {
            response = streamToString(is);
            arrayJSON = new JSONArray(response);

            if(arrayJSON!=null&&arrayJSON.length()>0){
                size=arrayJSON.length();
                userList = new ArrayList<User>(size);
                for (i=0;i<size;i++){
                    objectJSON = arrayJSON.getJSONObject(i);
                    if(objectJSON==null||(!objectJSON.has("avatar_url"))||TextUtils.isEmpty(objectJSON.getString("avatar_url"))
                            ||(!objectJSON.has("login"))||TextUtils.isEmpty(objectJSON.getString("login")))
                            continue;
                    userList.add(new User(objectJSON.getString("avatar_url"),objectJSON.getString("login")));
                }
            }
        }catch (Exception e) {
            return new ResultResponce(false, null);
        }
        return new ResultResponce(true, userList);
    }

    protected String streamToString(InputStream is) throws IOException {
        String string = "";
        if (is != null) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
            } finally {
                is.close();
            }
            string = stringBuilder.toString();
        }
        return string;
    }
}
