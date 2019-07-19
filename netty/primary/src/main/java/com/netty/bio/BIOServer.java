package com.netty.bio;

import org.apache.commons.io.IOUtils;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {



    public static void main(String[] args) throws Exception{
        //start();
        //startThread();
        startThreadPool();
    }

    public static void start() throws Exception{
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(8080));

        while(true){
            Socket socket = server.accept();
            String input = IOUtils.toString(socket.getInputStream(),(String) null);
            System.out.println(Thread.currentThread().getName()+" get client info:"+input);
        }
    }

    public static void startThread() throws Exception{
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(8080));
        System.out.println("--1");
        while(true){
            Socket socket = server.accept();
            System.out.println("--2");
            new Thread(()->{
                try{
                    System.out.println("--3");
                    String input = IOUtils.toString(socket.getInputStream(),(String) null);
                    System.out.println(Thread.currentThread().getName()+" get client info:"+input);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }).start();

        }
    }

    public static void startThreadPool() throws Exception{
        ExecutorService service = Executors.newFixedThreadPool(2);
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(8080));
        System.out.println("--1");
        while(true){
            Socket socket = server.accept();
            System.out.println("--2");
            service.execute(new Thread(()->{
                try{
                    System.out.println("--3");
                    String input = IOUtils.toString(socket.getInputStream(),(String) null);
                    System.out.println(Thread.currentThread().getName()+" get client info:"+input);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }));


        }
    }
}
