package com.chulm.notification;

import com.chulm.notification.firebase.message.Notification;
import com.chulm.notification.firebase.message.Result;
import com.chulm.notification.firebase.service.SynchronousProvider;
import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;

public class SynchronousProviderTest {

    @Test
    public void send(){

        Notification notification = Notification.builder().title("title")
                                                          .body("body")
                                                          .build();

        String[] devices = new String[]{"fIvLSpu6Eh4:APA91bHlGHiB_8IM00GRKlSV_soV-L6ARi3A1Sn3-mg9uK9Rpypim9TraM9WqNE_ncXN9jUG6hXy7rPi-7n9aBzWuFyB_7kfRY_XGgfR6-sozKyUHc8o5u3Z7IzVb7snllY_kP1x1Gja"};
        String apiKey = "AIzaSyAbsdAxWAIZAXnTR12mbAhe6GUH3yyIx7E";

        try {
            SynchronousProvider synchronousProvider = new SynchronousProvider(apiKey);
            Result result = synchronousProvider.send(notification, devices);
            System.out.println(result.toString());
            Assert.assertEquals(result.getSuccessCount(),0);
            Assert.assertEquals(result.getFailCount(),1);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}
