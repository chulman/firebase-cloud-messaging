package com.chulm.notification.firebase.service;

import com.chulm.notification.firebase.message.Notification;
import com.chulm.notification.firebase.service.netty.HttpProviderInitializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ReactiveProvider {

    private final String host = "fcm.googleapis.com";
    private final int port = 443;

    private final String uri = "https://fcm.googleapis.com/fcm/send";
    private final ObjectMapper mapper = new ObjectMapper();

    EventLoopGroup group;
    String apiKey;
    ChannelFuture cf;

    public ReactiveProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean connect() throws InterruptedException {

        SslContext sslCtx = null;
        try {
            sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        } catch (SSLException e1) {
            e1.printStackTrace();
        }

        group = new NioEventLoopGroup(1);
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).handler(new HttpProviderInitializer(sslCtx, group));

        cf = b.connect(host, port).sync();
        return cf.isSuccess();
    }

    public void close() {
        cf.channel().close();
        group.shutdownGracefully();

    }

    public ChannelFuture request(Notification notification, String[] devices) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("registration_ids", devices);
        map.put("notification", notification);

        HttpRequest request = null;
        String payload = mapper.writeValueAsString(map);
        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri,
                                            Unpooled.copiedBuffer(payload.getBytes(Charset.defaultCharset())));

        request.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, payload.length());
        request.headers().set(HttpHeaderNames.AUTHORIZATION, "key=" + apiKey);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CACHE_CONTROL, HttpHeaderValues.NO_CACHE);

        return cf.channel().writeAndFlush(request);
    }

}
