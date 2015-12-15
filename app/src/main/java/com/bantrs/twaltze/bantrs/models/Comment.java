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

public class Comment extends API {
    private String url;

    private String cid;
    public String comment;
    public String rid;
    public User author;
    public String createdAt;

    public Comment(JSONObject data) {
        try {
            url = api + "comment/";

            cid = data.getString("cid");
            comment = data.getString("comment");
            author = new User(data.getJSONObject("author"));
            createdAt = data.getString("createdAt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Comment(String comment, String rid) {
        this.url = api + "comment/";
        this.comment = comment;
        this.rid = rid;
    }

    public Comment create() throws Exception {
        System.out.println("create comment");

        RequestBody body = new FormEncodingBuilder()
                .add("comment", comment)
                .add("room", rid)
                .build();

        APIResponse response = post(url, body);

        Comment comment = new Comment(response.getObjectData());

        return comment;
    }
}
