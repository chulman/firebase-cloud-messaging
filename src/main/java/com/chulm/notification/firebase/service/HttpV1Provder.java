package com.chulm.notification.firebase.service;

// Firebase Cloud Messaging Http v1 API Provider -

import com.chulm.notification.firebase.message.Notification;
import com.chulm.notification.firebase.utils.FirebaseURI;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpV1Provder {


    private final ObjectMapper MAPPER = new ObjectMapper();

    private HttpURLConnection conn;
    private String token;

    private String adminFiles;
    private String projectName;


    public HttpV1Provder(String adminFiles, String projectName) throws IOException{
        this.adminFiles = adminFiles;
        this.projectName = projectName;
        token = getAccessToken(adminFiles);
    }

    private static String getAccessToken(String adminFiles)  throws IOException {

        FileInputStream fileInputStream = new FileInputStream(adminFiles);
        GoogleCredential googleCredential= GoogleCredential.fromStream(fileInputStream)
                                                           .createScoped(Arrays.asList(FirebaseURI.V1_AUTH_URL));
        googleCredential.refreshToken();
        return googleCredential.getAccessToken();
    }


    public String testSend() throws IOException {

        URL url = null;
        url = new URL(FirebaseURI.getV1SendURI(projectName));

        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod(HttpMethod.POST.name());
        conn.setRequestProperty(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString());
        conn.setRequestProperty(HttpHeaderNames.AUTHORIZATION.toString(), "Bearer " + token);
        conn.setRequestProperty(HttpHeaderNames.CONNECTION.toString(), HttpHeaderValues.KEEP_ALIVE.toString());

        OutputStream os = conn.getOutputStream();



        Map<String, Object> messageMap = new HashMap<>();
        Map<String, Object> notificationMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("title","title");
        dataMap.put("body","body");


        notificationMap.put("notification",dataMap);
        notificationMap.put("token","APA91bGU0aVG9G1fAVLrv_B5WUrdpikBG3T1mwdmcNgu_5Qv2FRr63rnrN5TkZsHqRhAbQzYMa1dCsCJKfKMLaKj0IYGZ3UHYHFbxvsnjiwMhtUdDNbsesO6rzZUN1_SMgRGRpslRR44");
//        notificationMap.put("condition","'CMCH' in topics || 'NOTI' in topics || 'ALL' in topics");
        messageMap.put("message", notificationMap);


        String payload = MAPPER.writeValueAsString(messageMap);
        System.out.println(payload);
        os.write(payload.getBytes(Charset.defaultCharset()));
        os.flush();
        os.close();

        return response();
    }

    public String response() throws IOException {
        String reads = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer responseBuffer = new StringBuffer();

        while ((reads = reader.readLine()) != null) {
            responseBuffer.append(reads);
        }

        reader.close();

        return responseBuffer.toString();
    }
}
