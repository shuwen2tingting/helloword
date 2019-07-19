package com.netty.bio;

import sun.util.locale.provider.TimeZoneNameUtility;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class BIOClient {

    public static void main(String[] args) throws Exception{
        for(int i=0;i<=100;i++){
            new Thread(()->{
                try{
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(8080));
                    socket.getOutputStream().write(("im sw "+Thread.currentThread().getName()).getBytes());
                    socket.getOutputStream().flush();
                    socket.getOutputStream().close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        }
        System.in.read();
    }

}
