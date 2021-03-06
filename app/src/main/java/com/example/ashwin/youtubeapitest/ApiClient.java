package com.example.ashwin.youtubeapitest;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vihaan on 1/7/15.
 */
public class ApiClient {

    public static final String tag = ApiClient.class.getSimpleName();

    private Request mRequest;

    public Response execute(Request request) throws IOException {
        mRequest = request;
        Response response = new Response();
        try {
            response = getResponse(mRequest);
        } catch (Exception e) {
            e.printStackTrace();
            //response.setResponseCode();
        } finally {
            response.setRequest(mRequest);
            return response;
        }
    }

    public Response getResponse(Request request) {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder;
        Response response = new Response();
        Map map;
        int responseCode = 0;

        HttpURLConnection connection = null;
        try {
            url = new URL(request.getUrl());

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(request.getRequestType());

            if (request.getHeaders() != null) {
                setConnectionHeaders(connection, request.getHeaders());
            }

            if (request.getBody() != null) {
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                JSONObject body;
                body = new JSONObject(request.getBody());
                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(body.toString());
                wr.close();
            } else if (request.getBodyJson() != null) {
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(request.getBodyJson().toString());
                wr.close();
            }

            connection.setReadTimeout(request.getRequestTimeout());
            connection.connect();

            responseCode = connection.getResponseCode();
            Log.v("RESPONSE CODE", "RESPONSE CODE: " + responseCode);

            stringBuilder = new StringBuilder();
            String line = null;

            if (responseCode == 401 || responseCode == 422 || responseCode == 404 || responseCode == 403) {
                if (connection.getErrorStream() != null)
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            } else
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((line = reader.readLine()) != null) {
//                stringBuilder.append(line + "\n");
                stringBuilder.append(line);
            }

            Log.v(tag, "\n" + "=================================================================================");

            Log.v(tag, "New Request" + "\n" + "=================================================================================");

            Log.v(tag, "Request method: " + request.getRequestType());
            Log.v(tag, "Request URL: " + url.toString());
            if (request.getHeaders() != null) {
                Gson gson = new Gson();
                String headers = gson.toJson(request.getHeaders());
                Log.v(tag, "Request Headers: " + "\n" + headers);

            }
            if (request.getBody() != null) {
                Log.v(tag, "Request Body: " + "\n" + request.getBody().toString());
            }
            if (request.getBodyJson() != null) {
                Log.v(tag, "Request Body Json " + request.getBodyJson());
            }
            Log.v(tag, "Response Code: " + responseCode);
            map = connection.getHeaderFields();

            if (map != null) {
                Gson gson = new Gson();
                String headers = gson.toJson(map);
                Log.v(tag, "Response Headers: " + "\n" + headers);
            }

            Log.v(tag, "Response Message: " + "\n" + stringBuilder.toString());

            response.setResponseCode(responseCode);
            response.setHeaders(map);
            response.setBody(stringBuilder.toString());

        } catch (Exception e) {
            e.printStackTrace();

            responseCode = connection.getResponseCode();

            JSONObject json = new JSONObject();
            json.put("errors", e.getMessage().toString());
            response.setResponseCode(responseCode);
            response.setBody(json.toString());


            //throw e;
        } finally {
            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            response.setRequest(request);

            return response;
        }
    }

    private void setConnectionHeaders(HttpURLConnection httpURLConnection, HashMap<String, String> headers) {

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            httpURLConnection.setRequestProperty(key, value);
        }

    }
}