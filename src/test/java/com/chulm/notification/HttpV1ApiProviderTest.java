package com.chulm.notification;

import com.chulm.notification.firebase.service.HttpV1Provder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.protocol.UriPatternMatcher;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpV1ApiProviderTest {


    @Test
    public void send(){
        final ObjectMapper mapper = new ObjectMapper();

        /**
         * Set Firebase Admin sdk json files Path.
         */
        String fileName = "";
        String adminFiles = System.getProperty("user.dir") + "/src/main/resources/" + fileName;

        try {
            /**
             * Input your Firebase Project Name.
             */
            String projectName = "";
            HttpV1Provder v1_provder = new HttpV1Provder(adminFiles, projectName);
            String response = v1_provder.testSend();

            Map<String,String> responseMap = mapper.readValue(response, Map.class);

            Assert.assertTrue(responseMap.keySet().contains("name"));

            String url = responseMap.get("name");

            String[] splitUrl = url.split("/");

            Assert.assertEquals(splitUrl[0],"projects");
            Assert.assertEquals(splitUrl[1],projectName);
            Assert.assertEquals(splitUrl[2],"messages");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
