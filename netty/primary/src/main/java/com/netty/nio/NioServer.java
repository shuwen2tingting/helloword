package com.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NioServer {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public NioServer() throws Exception{
        serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.configureBlocking(false);

        selector = Selector.open();

        serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);

        serverSocketChannel.socket().bind(new InetSocketAddress(7777));

        handleKeys();
    }

    private void handleKeys() throws Exception{
        while(true){
            if(selector.select()>0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if(key.isValid()){
                        handleKey(key);
                    }
                }
            }
        }
    }

    private void handleKey(SelectionKey key) throws Exception{
        if(key.isAcceptable()){
            handleAcceptable(key);
        }
        if(key.isReadable()){
            handleReadable(key);
        }
        if(key.isWritable()){
            handleWritable(key);
        }
    }

    private void handleAcceptable(SelectionKey key) throws Exception{
        SocketChannel clientSocketChannel = ((ServerSocketChannel) key.channel()).accept();
        // 配置为非阻塞
        clientSocketChannel.configureBlocking(false);
        // log
        System.out.println("接受新的 Channel");
        // 注册 Client Socket Channel 到 Selector
        clientSocketChannel.register(selector, SelectionKey.OP_READ, new ArrayList<String>());
    }

    private void handleReadable(SelectionKey key) throws Exception{
        // Client Socket Channel
        SocketChannel clientSocketChannel = (SocketChannel) key.channel();
        // 读取数据
        ByteBuffer readBuffer = CodecUtil.read(clientSocketChannel);
        // 处理连接已经断开的情况
        if (readBuffer == null) {
            System.out.println("断开 Channel");
            clientSocketChannel.register(selector, 0);
            return;
        }
        // 打印数据
        if (readBuffer.position() > 0) {
            String content = CodecUtil.newString(readBuffer);
            System.out.println("读取数据：" + content);

            // 添加到响应队列
            List<String> responseQueue = (ArrayList<String>) key.attachment();
            responseQueue.add("响应：" + content);
            // 注册 Client Socket Channel 到 Selector
            clientSocketChannel.register(selector, SelectionKey.OP_WRITE, key.attachment());
        }
    }

    private void handleWritable(SelectionKey key) throws Exception{
// Client Socket Channel
        SocketChannel clientSocketChannel = (SocketChannel) key.channel();

        // 遍历响应队列
        List<String> responseQueue = (ArrayList<String>) key.attachment();
        for (String content : responseQueue) {
            // 打印数据
            System.out.println("写入数据：" + content);
            // 返回
            CodecUtil.write(clientSocketChannel, content);
        }
        responseQueue.clear();

        // 注册 Client Socket Channel 到 Selector
        clientSocketChannel.register(selector, SelectionKey.OP_READ, responseQueue);
    }

    public static void main(String[] args) throws Exception {
        NioServer server = new NioServer();
    }

}