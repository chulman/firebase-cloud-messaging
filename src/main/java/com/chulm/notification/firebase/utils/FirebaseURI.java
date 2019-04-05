package com.chulm.notification.firebase.utils;

public class FirebaseURI {

    public static String V1_AUTH_URL = "https://www.googleapis.com/auth/firebase.messaging";

    public static String getV1SendURI(String projectName) {
        return "https://fcm.googleapis.com/v1/projects/" + projectName + "/messages:send";
    }
}
