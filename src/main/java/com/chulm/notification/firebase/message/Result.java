package com.chulm.notification.firebase.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    HttpResponseStatus httpResponseStatus;

    String multicastId;
    long successCount;
    long canonicalCount;
    long failCount;

    List<String> resultsList = new ArrayList<>();
}

