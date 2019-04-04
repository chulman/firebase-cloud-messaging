package com.chulm.notification;

import com.chulm.notification.firebase.message.Notification;
import com.chulm.notification.firebase.service.ReactiveProvider;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ReactiveProviderTest {

    public static void main(String[] args){
        Notification notification = Notification.builder().title("title")
                .body("body")
                .build();

        String[] devices = new String[]{"fIvLSpu6Eh4:APA91bHlGHiB_8IM00GRKlSV_soV-L6ARi3A1Sn3-mg9uK9Rpypim9TraM9WqNE_ncXN9jUG6hXy7rPi-7n9aBzWuFyB_7kfRY_XGgfR6-sozKyUHc8o5u3Z7IzVb7snllY_kP1x1Gja"};
        String apiKey = "AIzaSyAbsdAxWAIZAXnTR12mbAhe6GUH3yyIx7E";

        try {
            ReactiveProvider reactiveProvider = new ReactiveProvider(apiKey);

            boolean connect = reactiveProvider.connect();
            Assert.assertEquals(connect,true);

            ChannelFuture future = reactiveProvider.request(notification, devices);
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    Assert.assertEquals(future.isSuccess(), true);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
