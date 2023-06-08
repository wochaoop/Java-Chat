package com.javachat;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        while (true) {
            try {
                connectToServer();
                break; // 如果连接成功，则跳出循环
            } catch (IOException e) {
                System.out.println("无法连接到服务器，尝试重新连接...");
                try {
                    Thread.sleep(3000); // 休眠3秒后尝试重新连接
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static void connectToServer() throws IOException {
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        if (socket.isConnected()) {
            System.out.println("成功连接到服务器...");
        }

        BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter serverWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        Thread serverThread = new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = serverReader.readLine()) != null) {
                    System.out.println("收到服务器消息: " + serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        while ((userInput = userInputReader.readLine()) != null) {
            serverWriter.println(userInput);
            System.out.println("已发送消息给服务器: " + userInput);
        }

        socket.close();
    }
}
