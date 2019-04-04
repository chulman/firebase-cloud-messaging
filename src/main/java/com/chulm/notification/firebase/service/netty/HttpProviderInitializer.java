package com.chulm.notification.firebase.service.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.ReadTimeoutHandler;



public class HttpProviderInitializer extends ChannelInitializer<SocketChannel>{
	
	SslContext sslCtx;
	EventLoopGroup group;
	final int MAX_TIMEOUT=5000;

	 public HttpProviderInitializer(SslContext sslCtx, EventLoopGroup group) {
	        this.sslCtx = sslCtx;
	        this.group = group;
	 }

	    @Override
	    public void initChannel(SocketChannel ch) {
	        ChannelPipeline p = ch.pipeline();
	        // Enable HTTPS 
	        if (sslCtx != null) {
	            p.addLast(sslCtx.newHandler(ch.alloc()));
	        }

	        p.addLast(new ReadTimeoutHandler(MAX_TIMEOUT));
	      
	        // convert to HTTP Response
	        p.addLast(new HttpClientCodec());
	        p.addLast(new HttpObjectAggregator(1048576));
	        p.addLast(new HttpResponseHandler(group, ch));
	    }
}
