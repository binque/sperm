package com.dirlt.java.netty;

import java.net.InetSocketAddress;
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
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

public class SimpleHTTPServer {
	public static class Handler extends SimpleChannelHandler {
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
			System.out.println("received channle id = "
					+ ctx.getChannel().getId().toString());
			Channel channel = e.getChannel();
			ChannelBuffer buffer = ChannelBuffers.buffer(32);
			buffer.writeBytes("Hello,World".getBytes());
			channel.write(buffer);
		}

		@Override
		public void writeComplete(ChannelHandlerContext ctx,
				WriteCompletionEvent e) {
			System.out.println("write complete channel id = "
					+ ctx.getChannel().getId().toString());
			ctx.getChannel().close();
		}

		@Override
		public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
			System.out.println("accepted channel id = "
					+ ctx.getChannel().getId().toString());
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
