package com.bantrs.twaltze.bantrs.models;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.FormEncodingBuilder;
import org.json.JSONObject;

import java.io.IOException;

public class Auth extends API {
    private static final Auth INSTANCE = new Auth();

    private String url;
    private boolean isLoggedIn = false;
    private String token;
    private User user;

    private Auth() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }

        url = api + "user/auth/";
    }

    public static Auth getInstance() {
        return INSTANCE;
    }

    public User login(String username, String password) throws Exception {
        System.out.println("login");

        RequestBody body = new FormEncodingBuilder()
                .add("username", username)
                .add("password", password)
                .build();

        APIResponse response = post(url, body);

        token = response.getObjectData().getString("authToken");
        isLoggedIn = true;
        user = new User(response.getObjectData().getJSONObject("user"));

        return user;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
