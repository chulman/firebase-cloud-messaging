package com.chulm.notification.firebase.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class AdminSdkManager {

    public void firebaseInitializer(String file){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            FirebaseOptions options = new FirebaseOptions.Builder()
                                                         .setCredentials(GoogleCredentials.fromStream(fileInputStream))
                                                         .build();

            FirebaseApp.initializeApp(options);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     *
     * @param topic
     * @param deviceToken : 1000개까지 가능
     */
    public TopicManagementResponse subscribeTopic(String topic, String... deviceToken){
        TopicManagementResponse response = null;
        List<String> registrationTokens = Arrays.asList(deviceToken);
        try {
            response = FirebaseMessaging.getInstance().subscribeToTopic(registrationTokens, topic);

        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     *
     * @param topic
     * @param deviceToken: 1000개 까지 가능
     */
    public TopicManagementResponse unsubscribeTopic(String topic, String... deviceToken){

        TopicManagementResponse response = null;
        List<String> unRegistrationTokens = Arrays.asList(deviceToken);

        try {
            response = FirebaseMessaging.getInstance().unsubscribeFromTopic(unRegistrationTokens, topic);

        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     *
     * @param token : Instance ID
     * @param apiKey : Server Key
     * @return
     */
    public String getInstanceInfo(String token,String apiKey){

        HttpURLConnection conn  = null;
        URL url = null;

        String uri = "https://iid.googleapis.com/iid/info/" + token + "?details=true";
        StringBuffer response = new StringBuffer();
        try {
            url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(HttpMethod.GET.name());
            conn.setRequestProperty(HttpHeaderNames.AUTHORIZATION.toString(), "key="+apiKey);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine = "";
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }

            reader.close();

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }


}


