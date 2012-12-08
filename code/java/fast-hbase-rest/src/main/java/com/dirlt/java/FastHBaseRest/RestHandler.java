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
    private AsyncClient client = new AsyncClient(); // each handler corresponding a channel.

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        HttpRequest request = (HttpRequest) e.getMessage();
        Channel channel = e.getChannel();
        StatStore.incRpcInCount();

        if (request.getUri().equals("/stat")) {
            // just return statistics.
            StatStore stat = StatStore.getInstance();
            HttpResponse response = new DefaultHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            String content = stat.toString();
            StatStore.addRpcOutBytes(content.length());
            response.setHeader("Content-Length", content.length());
            ChannelBuffer buffer = ChannelBuffers.buffer(content.length());
            buffer.writeBytes(content.getBytes());
            response.setContent(buffer);
            channel.write(response);
            return;
        }

        client.code = AsyncClient.Status.kHttpRequest;
        client.channel = channel;
        client.httpRequest = request;
        CpuWorkerPool.getInstance().submit(client);
    }

    @Override
    public void writeComplete(ChannelHandlerContext ctx,
                              WriteCompletionEvent e) {
        StatStore.incRpcOutCount();
        // not handling.
        // don't close it.
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
        RestServer.logger.debug("connection open");
        StatStore.incConnectionCount();
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
        RestServer.logger.debug("connection closed");
        StatStore.decConnectionCount();
        e.getChannel().close();
    }
}