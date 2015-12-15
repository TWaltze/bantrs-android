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
import java.util.ArrayList;
import java.util.List;

public class Room extends API {
    private String url;

    private String rid;
    public String title;
    public User author;
    public String topic;
    public String createdAt;

    public Room(JSONObject data) {
        try {
            url = api + "room/";

            rid = data.getString("rid");
            title = data.getString("title");
            author = new User(data.getJSONObject("author"));
            topic = data.getJSONObject("topic").getString("content");
            createdAt = data.getString("createdAt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Room(String title, String url) {
        this.url = api + "room/";
        this.title = title;
        this.topic = url;
    }

    public static Room getRoom(String rid) throws Exception {
        System.out.println("get room");

        APIResponse response = get(api + "room/" + rid);

        if (response.isSuccessful()) {
            Room room = new Room(response.getObjectData());
            System.out.println("Room rid: " + room.getID());
            return room;
        } else {
            return null;
        }
    }

    public Room create() throws Exception {
        System.out.println("create room");

        RequestBody body = new FormEncodingBuilder()
                .add("title", title)
                .add("topic", topic)
                .build();

        APIResponse response = post(url, body);

        Room room = new Room(response.getObjectData());

        return room;
    }

    public String getID() {
        return rid;
    }

    public List<Comment> getComments() throws Exception {
        System.out.println("getComments");

        APIResponse response = get(url + rid + "/comments");

        if (response.isSuccessful()) {
            JSONArray body = response.getArrayData();

            List<Comment> comments = new ArrayList<Comment>(body.length());
            for (int i = 0; i < body.length(); i++) {
                JSONObject comment = body.getJSONObject(i);
                comments.add(new Comment(comment));
            }

            return comments;
        } else {
            return new ArrayList<Comment>();
        }
    }
}
