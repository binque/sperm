package com.dirlt.java.FastHBaseRest;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: dirlt
 * Date: 12/8/12
 * Time: 1:44 AM
 * To change this template use File | Settings | File Templates.
 */

public class RestHandler extends SimpleChannelHandler {
    private static final Set<String> allowedPath = new TreeSet<String>();

    static {
        allowedPath.add("/stat");
        allowedPath.add("/read");
        allowedPath.add("/multi-read");
        allowedPath.add("/write");
        allowedPath.add("/multi-write");
    }

    private Configuration configuration;
    private AsyncClient client; // binding to the channel pipeline.

    public RestHandler(Configuration configuration) {
        this.configuration = configuration;
        client = new AsyncClient(configuration); // each handler corresponding a channel or a connection.
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        RestServer.logger.debug("message received");
        HttpRequest request = (HttpRequest) e.getMessage();
        Channel channel = e.getChannel();
        StatStore stat = StatStore.getInstance();
        String path = null;

        try {
            URI uri = new URI(request.getUri());
            path = uri.getPath();
        } catch (URISyntaxException ex) {
            // ignore.
            stat.addCounter("uri.invalid.count", 1);
            channel.close();
            return;
        }

        if (!allowedPath.contains(path)) {
            stat.addCounter("uri.unknown.count", 1);
            channel.close(); // just close the connection.
            return;
        }

        // as stat, we can easily handle it.
        if (path.equals("/stat")) {
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
        client.subRequest = false;
        client.channel = channel;
        client.httpRequest = request;
        client.path = path;
        client.run();
    }

    @Override
    public void writeComplete(ChannelHandlerContext ctx,
                              WriteCompletionEvent e) {
        RestServer.logger.debug("write completed");

        if (client.code != AsyncClient.Status.kStat) {
            StatStore.getInstance().addCounter("rpc.out.count", 1);
        }
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
        RestServer.logger.debug("connection open");

        StatStore.getInstance().addCounter("session.in.count", 1);
        client.sessionStartTimestamp = System.currentTimeMillis();

    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
        RestServer.logger.debug("connection closed");

        StatStore.getInstance().addCounter("session.out.count", 1);
        client.sessionEndTimestamp = System.currentTimeMillis();
        StatStore.getInstance().addCounter("session.duration",
                client.sessionEndTimestamp - client.sessionStartTimestamp);
        e.getChannel().close();
    }
}