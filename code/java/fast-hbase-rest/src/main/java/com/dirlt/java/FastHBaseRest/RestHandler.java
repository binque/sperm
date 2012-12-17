package com.dirlt.java.FastHBaseRest;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;

/**
 * Created with IntelliJ IDEA.
 * User: dirlt
 * Date: 12/8/12
 * Time: 1:44 AM
 * To change this template use File | Settings | File Templates.
 */

public class RestHandler extends SimpleChannelHandler {
    private AsyncClient client = new AsyncClient(); // each handler corresponding a channel or a connection.

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        RestServer.logger.debug("message received");

        HttpRequest request = (HttpRequest) e.getMessage();
        Channel channel = e.getChannel();
        StatStore stat = StatStore.getInstance();
        client.code = AsyncClient.Status.kStat;

        if (request.getUri().equals("/stat")) {
            HttpResponse response = new DefaultHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            // handle it in the same thread.
            String content = stat.toString();
            response.setHeader("Content-Length", content.length());
            ChannelBuffer buffer = ChannelBuffers.buffer(content.length());
            buffer.writeBytes(content.getBytes());
            response.setContent(buffer);
            channel.write(response);
            return;
        }

        stat.addCounter("rpc.in.count", 1);
        client.code = AsyncClient.Status.kHttpRequest;
        client.channel = channel;
        client.httpRequest = request;
        client.queryStartTimestamp = RestServer.now();
        CpuWorkerPool.getInstance().submit(client);
    }

    @Override
    public void writeComplete(ChannelHandlerContext ctx,
                              WriteCompletionEvent e) {
        RestServer.logger.debug("write completed");

        if (client.code != AsyncClient.Status.kStat) {
            StatStore.getInstance().addCounter("rpc.out.count", 1);
            client.queryEndTimestamp = RestServer.now();
            StatStore.getInstance().addCounter("rpc.query.duration", client.queryEndTimestamp - client.queryStartTimestamp);
        }
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
        RestServer.logger.debug("connection open");

        StatStore.getInstance().addCounter("session.in.count", 1);
        client.sessionStartTimestamp = RestServer.now();

    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
        RestServer.logger.debug("connection closed");

        StatStore.getInstance().addCounter("session.out.count", 1);
        client.sessionEndTimestamp = RestServer.now();
        StatStore.getInstance().addCounter("session.duration",
                client.sessionEndTimestamp - client.sessionStartTimestamp);
        e.getChannel().close();

    }
}