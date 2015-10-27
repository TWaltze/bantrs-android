package com.bantrs.twaltze.bantrs.models;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class User extends API {
    private String url;

    private String uid;
    public String username;
    public String email;

    public User(String username) {
        url = api + "user/";
        this.username = username;

        getUser();
    }

    public User(JSONObject data) {
        System.out.println("JSONObject");
        System.out.println(data);
        try {
            uid = data.getString("uid");
            username = data.getString("username");
            email = data.getString("email");

            System.out.println(username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getUser() {
        Request request = new Request.Builder()
                .url(url + username)
                .build();

        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Request request, IOException throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                System.out.println(response.body().string());
            }
        });
    }

    public String getRooms() throws Exception {
        Request request = new Request.Builder()
                .url(url + username + "/rooms")
                .header("Authorization", "12ac39e5c267e18ed363dc80ccfcd329952f0c1b2ce2e89c5b317dc1f5eb5eb7")
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            System.out.println("Successful.");

            String body = response.body().string();
            System.out.println(body);
            return body;
        } else {
            System.out.println("Failed.");
            return "Failed to get rooms.";
        }
    }
}
