package com.chulm.notification;

import com.chulm.notification.firebase.service.AdminSdkManager;
import com.google.firebase.messaging.TopicManagementResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FirebaseAdminTest {

    AdminSdkManager adminSdkManager;

    @Before
    public void initializer() {
        String files = System.getProperty("user.dir") + "/src/main/resources/xpush-9aba8-firebase-adminsdk-q7zfh-a09c11b3ba.json";
        adminSdkManager = new AdminSdkManager();
        adminSdkManager.firebaseInitializer(files);
    }

    @Test
    public void subscribe() {
        String topic = "news";

        String[] devices = new String[]{
                "dSjP_DM8QDg:APA91bHFka4cZfE1mFbCEG9qdsXnGld9e_ZW7O3d8RLpjUt99I8E6a4ruk_MOmzwaVEoBMoPiqOU0AwJx1dCFtppxOfpXlWJXX5JZQEVikQkVYtuiyxVlFmFSghoOPs4mNL5_ZAG6I8g",
        };
        TopicManagementResponse response = adminSdkManager.subscribeTopic(topic,devices);
        System.out.println("sucess:" + response.getSuccessCount());
        System.out.println("fail:" + response.getFailureCount());

        response.getErrors().stream().forEach(error -> {
                    System.out.println(error.getReason());
                }
        );

        Assert.assertEquals(response.getSuccessCount(), 1);
        Assert.assertEquals(response.getFailureCount(), 0);
    }

    @Test
    public void unsubscribe() {
        String topic = "news";

        String[] devices = new String[]{
                "dSjP_DM8QDg:APA91bHFka4cZfE1mFbCEG9qdsXnGld9e_ZW7O3d8RLpjUt99I8E6a4ruk_MOmzwaVEoBMoPiqOU0AwJx1dCFtppxOfpXlWJXX5JZQEVikQkVYtuiyxVlFmFSghoOPs4mNL5_ZAG6I8g",
        };
        TopicManagementResponse response = adminSdkManager.unsubscribeTopic(topic,devices);
        System.out.println("sucess:" + response.getSuccessCount());
        System.out.println("fail:" + response.getFailureCount());

        response.getErrors().stream().forEach(error -> {
                    System.out.println(error.getReason());
                }
        );

        Assert.assertEquals(response.getSuccessCount(), 1);
        Assert.assertEquals(response.getFailureCount(), 0);
    }

    @Test
    public void getInstanceInfo(){
        String response = adminSdkManager.getInstanceInfo("fIvLSpu6Eh4:APA91bHlGHiB_8IM00GRKlSV_soV-L6ARi3A1Sn3-mg9uK9Rpypim9TraM9WqNE_ncXN9jUG6hXy7rPi-7n9aBzWuFyB_7kfRY_XGgfR6-sozKyUHc8o5u3Z7IzVb7snllY_kP1x1Gja","AIzaSyCLvtvNiV7VmavVw4GeKUErga66YbyRs6U");
        System.err.println(response);
    }
}
