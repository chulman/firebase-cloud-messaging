package com.chulm.notification.firebase.service;

import com.chulm.notification.firebase.message.Notification;
import com.chulm.notification.firebase.message.Result;
import com.chulm.notification.firebase.utils.ResponseKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class SynchronousProvider {

    private final URL url = new URL("https://fcm.googleapis.com/fcm/send");
    private final ObjectMapper mapper = new ObjectMapper();
    private String apiKey;

    public SynchronousProvider(String apiKey) throws MalformedURLException {
        this.apiKey = apiKey;
    }

    public Result send(Notification notification, String[] devices) {

        Result result = null;

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(HttpMethod.POST.name());
            conn.setRequestProperty(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString());
            conn.setRequestProperty(HttpHeaderNames.AUTHORIZATION.toString(), "key=" + getApiKey());
            conn.setRequestProperty(HttpHeaderNames.CONNECTION.toString(), HttpHeaderValues.KEEP_ALIVE.toString());

            conn.setInstanceFollowRedirects(false);
            conn.setDefaultUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);


            write(conn.getOutputStream(), notification, devices);


            int responseCode = conn.getResponseCode();

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine = "";
            StringBuffer response = new StringBuffer();

            while ((inputLine = inputReader.readLine()) != null) {
                response.append(inputLine);
            }

            close(inputReader);

            result = makeFcmResult(responseCode, response.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    public String getApiKey() {
        return apiKey;
    }

    private void write(OutputStream outputStream, Notification notification, String[] devices) {
        try {

            Map<String, Object> notificationMap = new HashMap<>();

            notificationMap.put("registration_ids", devices);
            notificationMap.put("notification", notification);

            outputStream.write(mapper.writeValueAsString(notificationMap).getBytes(Charset.defaultCharset()));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(outputStream);
        }
    }


    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Result makeFcmResult(int responseCode, String response) {
        Result result = null;
        try {
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            Map<String, Object> multicastResults = mapper.readValue(response, Map.class);

            //TODO RESULT LIST = Success Device is messsage_Id get, Failure device is error get

            result = Result.builder()
                    .multicastId(multicastResults.get(ResponseKey.MULTICAST_ID).toString())
                    .successCount(Long.parseLong(multicastResults.get(ResponseKey.SUCCESS).toString()))
                    .failCount(Long.parseLong(multicastResults.get(ResponseKey.FAILURE).toString()))
                    .canonicalCount(Long.parseLong(multicastResults.get(ResponseKey.CANONICAL_IDS).toString()))
                    .httpResponseStatus(HttpResponseStatus.valueOf(responseCode))
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
