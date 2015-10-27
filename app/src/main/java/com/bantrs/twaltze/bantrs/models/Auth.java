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
    private String url;
    private boolean isLoggedIn = false;

    public Auth() {
        url = api + "user/auth/";
    }

    public User login(String username, String password) throws Exception {
//        RequestBody formBody = new FormEncodingBuilder()
//                .add("username", username)
//                .add("password", password)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(formBody)
//                .build();

//        client.newCall(request).enqueue(new Callback() {
//            public void onFailure(Request request, IOException throwable) {
//                throwable.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                if (!response.isSuccessful()) {
//                    System.out.println("Invalid login credentials.");
//                } else {
//                    String body = response.body().string();
//                    System.out.println("Successful login.");
//                }
//            }
//        });

//        Response response = client.newCall(request).execute();
//
//        if (response.isSuccessful()) {
//            System.out.println("Successful login.");
//
//            String body = response.body().string();
//            User user = new User(getData(body).getJSONObject("user"));
//
//            return user;
//        } else {
//            System.out.println("Invalid login credentials.");
//
//            return null;
//        }

        RequestBody body = new FormEncodingBuilder()
                .add("username", username)
                .add("password", password)
                .build();

        APIResponse response = post(url, body);

        User user = new User(response.getData().getJSONObject("user"));

        return user;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
