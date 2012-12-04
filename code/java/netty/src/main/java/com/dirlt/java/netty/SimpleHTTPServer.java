package com.dirlt.java.netty;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.WriteCompletionEvent;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

public class SimpleHTTPServer {
	public static class Metric {
		private Map<String,Long> counter = new HashMap<String,Long>();
		private Map<String,String> status = new HashMap<String,String>();
		private static Metric instance = new Metric();
		public static Metric getInstance() {
			return instance;
		}
		public void addCounter(String name, long value) {
			synchronized(counter) {
				if(counter.containsKey(name)){
					counter.put(name, counter.get(name) + value);
				} else {
					counter.put(name, value);
				}
			}
		}
		public long getCounter(String name) {
			synchronized(counter) {
				if(!counter.containsKey(name)) {
					return 0L;
				}else {
					return counter.get(name);
				}
			}
		}
		public void updateStatus(String name,String value) {
			synchronized(status) {
				status.put(name, value);
			}
		}
		public void getStatus(String name) {
			synchronized(status) {
				status.get(name);
			}
		}
	}
	public static class Handler extends SimpleChannelHandler {
		// initialize a handler every time!WTF.
		{
			System.out.println("do init...");
		}
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
			Metric metric = Metric.getInstance();	
			metric.addCounter("rpc-count", 1);
			Metric.getInstance().updateStatus("session#" + ctx.getChannel().getId().toString(),"handling");
			
			System.out.printf("===session===\n");		
			HttpRequest request = (HttpRequest)e.getMessage();
			System.out.println("method = " + request.getMethod());
			System.out.println("uri = " + request.getUri());
			System.out.println("-->header<--");
			for(String key:request.getHeaderNames()){
				System.out.printf("%s = %s\n", key, request.getHeader(key));
			}
			Channel channel = e.getChannel();
			ChannelBuffer buffer = ChannelBuffers.buffer(32);
			buffer.writeBytes("Hello,World".getBytes());
			channel.write(buffer);
		}

		@Override
		public void writeComplete(ChannelHandlerContext ctx,
				WriteCompletionEvent e) {			
			Metric.getInstance().updateStatus("session#" + ctx.getChannel().getId().toString(),"handled");
			System.out.println("handled");
			e.getChannel().close();
		}

		@Override
		public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
			System.out.println("accepted");
			Metric.getInstance().updateStatus("session#" + ctx.getChannel().getId().toString(),"open");
		}
		
		@Override
		public void channelClosed(
	            ChannelHandlerContext ctx, ChannelStateEvent e) {
			System.out.println("closed");
			Metric.getInstance().updateStatus("session#" + ctx.getChannel().getId().toString(),"closed");
			ctx.getChannel().close();
		}
	}

	public static void main(String[] args) {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newFixedThreadPool(4), // boss
				Executors.newFixedThreadPool(16) // worker. must be >=2 * CPU cores.
		);
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("decoder", new HttpRequestDecoder());
				pipeline.addLast("encoder", new HttpResponseEncoder());
				pipeline.addLast("handler", new Handler());
				return pipeline;
			}
		});
		bootstrap.bind(new InetSocketAddress(8001));
	}
}
