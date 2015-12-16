package com.bantrs.twaltze.bantrs.models;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import com.google.gson.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.json.JSONObject;

public abstract class API {
    public static final String api = "http://10.0.2.2:3000/";
    protected final OkHttpClient client = new OkHttpClient();

    public static JSONObject getMeta(String json) {
        try {
            return new JSONObject(json).getJSONObject("meta");
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e);
        }

        return null;
    }

    public static JSONObject getData(String json) {
        try {
            return new JSONObject(json).getJSONObject("data");
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e);
        }

        return null;
    }

    public static int getCode(String json) {
        try {
            return getMeta(json).getInt("code");
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e);
        }

        return -1;
    }

    public static APIResponse get(String url) throws Exception {
        Request.Builder builder = new Request.Builder().url(url);

        return makeRequest(builder);
    }

    public static APIResponse post(String url, RequestBody body) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);

        return makeRequest(builder);
    }

    private static APIResponse makeRequest(Request.Builder builder) throws Exception {
        if (Auth.getInstance().isLoggedIn()) {
            System.out.println("Authenticated Request");
            System.out.println(Auth.getInstance().getToken());
            builder.header("Authorization", Auth.getInstance().getToken());
        } else {
            System.out.println("Not Authenticated Request");
        }

        Request request = builder.build();
        OkHttpClient client = new OkHttpClient();

        return new APIResponse(client.newCall(request).execute());
    }

    protected static class APIResponse {
        private boolean successful;
        private JSONObject meta;
        private JSONObject dataAsObject;
        private JSONArray dataAsArray;

        public APIResponse(Response response) {
            try {
                successful = response.isSuccessful();

                String body = response.body().string();
                System.out.println(body);
                JSONObject json = new JSONObject(body);

                meta = json.getJSONObject("meta");

                Object holder = json.get("data");
                if (holder instanceof JSONObject) {
                    dataAsObject = (JSONObject)holder;
                } else if (holder instanceof JSONArray) {
                    dataAsArray = (JSONArray)holder;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public JSONObject getMeta() {
            return meta;
        }

        public JSONObject getObjectData() {
            return dataAsObject;
        }

        public JSONArray getArrayData() {
            return dataAsArray;
        }

        public boolean isSuccessful() {
            return successful;
        }
    }
}
