package com.bantrs.twaltze.bantrs.models;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
        try {
            url = api + "user/";
            uid = data.getString("uid");
            username = data.getString("username");
            email = data.getString("email");
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

    public List<Room> getRooms() throws Exception {
        System.out.println("getRooms");

        APIResponse response = get(url + username + "/rooms");

        if (response.isSuccessful()) {
            JSONArray body = response.getArrayData();

            List<Room> rooms = new ArrayList<Room>(body.length());
            for (int i = 0; i < body.length(); i++) {
                JSONObject room = body.getJSONObject(i);
                rooms.add(new Room(room));
            }

            return rooms;
        } else {
            return new ArrayList<Room>();
        }
    }
}
