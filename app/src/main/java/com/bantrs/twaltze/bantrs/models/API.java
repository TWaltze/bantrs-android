package com.bantrs.twaltze.bantrs.models;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import com.google.gson.*;

import org.json.JSONException;
import org.json.JSONObject;

import org.json.JSONObject;

public abstract class API {
    public static final String api = "http://10.0.2.2:3000/";
    protected final OkHttpClient client = new OkHttpClient();
    protected final Gson gson = new Gson();

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

    public APIResponse get(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();

        return new APIResponse(client.newCall(request).execute());
    }

    public APIResponse post(String url, RequestBody body) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return new APIResponse(client.newCall(request).execute());
    }

    protected class APIResponse {
        private boolean successful;
        private JSONObject meta;
        private JSONObject data;

        public APIResponse(Response response) {
            try {
                successful = response.isSuccessful();

                String body = response.body().string();
                JSONObject json = new JSONObject(body);

                meta = json.getJSONObject("meta");
                data = json.getJSONObject("data");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public JSONObject getMeta() {
            return meta;
        }

        public JSONObject getData() {
            return data;
        }

        public boolean isSuccessful() {
            return successful;
        }
    }
}
