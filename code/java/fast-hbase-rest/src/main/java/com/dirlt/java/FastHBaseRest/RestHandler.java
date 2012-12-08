package com.dirlt.java.FastHBaseRest;

import org.jboss.netty.channel.*;

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
        MetricStore.incRpcInCount();
        client.code = AsyncClient.kHandleRequest;
        client.channel = e.getChannel();
        client.httpRequest = (HttpRequest) e.getMessage();
        CpuWorkerPool.getInstance().submit(client);
    }

    @Override
    public void writeComplete(ChannelHandlerContext ctx,
                              WriteCompletionEvent e) {
        // not handling.
        // don't close it.
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
        MetricStore.incConnectionCount();
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
        MetricStore.decConnectionCount();
        client.code = AsyncClient.kClosed;
        CpuWorkerPool.getInstance().submit(client);
    }
}